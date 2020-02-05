package com.genesis.api;

import org.ethereum.solidity.Abi;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbiTool {

    private Abi abiDef;

    /**
     * 初始化abiDefinition
     * @param abiDefinition
     */
    public AbiTool(String abiDefinition){
        abiDef = Abi.fromJson(abiDefinition);
    }

    /**
     * 根据abiCode和encodedFunc解析function
     * @param encodedFunc
     * @return
     */
    public Function decodeFunction(String encodedFunc){
        if (!encodedFunc.startsWith("0x")){
            encodedFunc = "0x" + encodedFunc;
        }

        String methodId = encodedFunc.substring(0, 10);
        String rawInput = encodedFunc.substring(10);
        for (Abi.Entry entry : this.abiDef){
            if (entry.type.name().equals("function") && buildMethodId(entry.formatSignature()).equals(methodId)) {
                List<TypeReference<?>> inputTypeRefer = findInputTypeReference(entry.inputs);
                List<Type> inputs = decodeInputs(rawInput, inputTypeRefer);
                Function func = new Function(entry.name, inputs, Collections.emptyList());
                return func;
            }
        }
        return null;
    }


    /**
     * 根据参数类型名称解析原始类型
     * @param typeName 参数类型， eg: uint256[], uint8[], address
     * @return
     */
    private static TypeReference<?> parseTypeReference(String typeName) {
        if (typeName.contains("[")) return parseArrayTypeRefer(typeName);
        if ("bool".equals(typeName)) return new TypeReference<Bool>() {};
        if ("address".equals(typeName)) return new TypeReference<Address>() {};
        if ("string".equals(typeName)) return new TypeReference<Utf8String>() {};
        if ("bytes".equals(typeName)) return new TypeReference<Bytes>() {};
        Class cl =  AbiTypes.getType(typeName);
        return TypeReference.create(cl);
    }


    /**
     * todo 目前默认uint256, 其他类型数组如何处理？
     * @param typeName
     * @return
     */
    private static TypeReference<?> parseArrayTypeRefer(String typeName) {
        int idx1 = typeName.indexOf("[");
        int idx2 = typeName.indexOf("]", idx1);
        TypeReference<?> elementTypeRefer = parseTypeReference(typeName.substring(0, idx1));
        if (idx1 + 1 == idx2) {
            return new TypeReference<DynamicArray<Uint256>>(){};
        }else{
            int staticArraySize =Integer.parseInt(typeName.substring(idx1+1, idx2));
            return new TypeReference.StaticArrayTypeReference<StaticArray<Uint256>>(staticArraySize) {};
        }
    }


    /**
     * 解析inputs
     * @param rawInput
     * @param inputTypeRefer1
     * @return
     */
    private List<Type> decodeInputs(String rawInput, List<TypeReference<?>> inputTypeRefer1){
        Function func = new Function("", Collections.emptyList(), inputTypeRefer1);
        List<TypeReference<Type>> inputTypeRefer2 = func.getOutputParameters();
        List<Type> inputs = FunctionReturnDecoder.decode(
                rawInput, inputTypeRefer2);
        return inputs;
    }


    /**
     * 获取input类型
     * @param inputs
     * @return
     */
    private List<TypeReference<?>> findInputTypeReference(List<Abi.Entry.Param> inputs){
        List<TypeReference<?>> inputTypeRefer = new ArrayList<>();
        for (Abi.Entry.Param param: inputs){
            inputTypeRefer.add(parseTypeReference(param.type.getName()));
        }
        return inputTypeRefer;
    }


    private static String buildMethodId(String methodSignature) {
        byte[] input = methodSignature.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }
}
