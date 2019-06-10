package com.rendez.api;

import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.Signature;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TransactionUtil {
    /**
     * 创建合约调用交易
     *
     * @param nonce
     * @param contractAddress
     * @param function 函数定义
     * @return
     */
    public static RawTransaction createCallContractTransaction(BigInteger nonce, String contractAddress, Function function){
        RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(100000000000L), contractAddress, FunctionEncoder.encode(function));
        return rtx;
    }

    /**
     * 创建部署合约交易
     *
     * @param binaryCode 二进制合约代码
     * @param constructorParameters 构建参数
     * @param nonce
     * @return
     */
    public static RawTransaction createDelopyContractTransaction(String binaryCode, List<Type> constructorParameters, BigInteger nonce){
        RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(100000000000L), null, binaryCode + FunctionEncoder.encodeConstructor(constructorParameters));
        return rtx;
    }

    /**
     * decode单笔链上交易
     *
     * @param txMsg 链上交易
     * @return 原始交易信息
     */
    public static SignedRawTransaction decodeTxMsg(String txMsg){
        SignedRawTransaction signedRawTransaction = (SignedRawTransaction) TransactionDecoder.decode(txMsg);
        return signedRawTransaction;
    }


    /**
     * encode交易
     *
     * @param tx
     * @return
     */
    public static byte[] encode(RawTransaction tx) {
        return TransactionEncoder.encode(tx);
    }

    /**
     * encode原始交易和签名
     *
     * @param tx
     * @return
     */
    public static byte[] encode(RawTransaction tx, PrivateKey privateKey){
        Signature signature =  CryptoUtil.generateSignature(tx, privateKey);
        return encodeWithSig(tx, signature);
    }


    /**
     * 使用已生成的签名encode交易
     *
     * @param tx
     * @return
     */
    public static byte[] encodeWithSig(RawTransaction tx, Signature sig) {
        List<RlpType> values = asRlpValues(tx, sig);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }




    private static List<RlpType> asRlpValues(RawTransaction rawTransaction, Signature sig) {

        List<RlpType> result = new ArrayList();
        result.add(RlpString.create(rawTransaction.getNonce()));
        result.add(RlpString.create(rawTransaction.getGasPrice()));
        result.add(RlpString.create(rawTransaction.getGasLimit()));
        String to = rawTransaction.getTo();
        if (to != null && to.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(rawTransaction.getValue()));
        byte[] data = Numeric.hexStringToByteArray(rawTransaction.getData());
        result.add(RlpString.create(data));
        if (sig != null) {
            result.add(RlpString.create(sig.getV()));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(sig.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(sig.getS())));
        }
        return result;
    }

}
