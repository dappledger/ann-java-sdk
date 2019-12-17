package com.genesis.api;

import com.genesis.api.crypto.PrivateKey;
import com.genesis.api.crypto.Signature;
import org.spongycastle.util.encoders.Hex;
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
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
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
        RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(1000000000L), contractAddress, FunctionEncoder.encode(function));
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
    	RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(1000000000L), null, binaryCode + FunctionEncoder.encodeConstructor(constructorParameters));
        return rtx;
    }
    
    /**
     * 创建kv交易
     *
     * @param nonce
     * @param key
     * @param value
     * @return
     */
    public static RawTransaction createPutKVTransaction(BigInteger nonce, String key,String value){
    	byte[] kvByte = encodeWithKV(key,value);
        RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(1000000000L), null, paddType(Numeric.toHexString(kvByte)));
        return rtx;
    }
    
    public static String paddType(String data) {
        byte[] strByte = Numeric.hexStringToByteArray(data);
        byte[] type = "kvTx-".getBytes();
        byte[] resp = new byte[strByte.length + type.length];
        System.arraycopy(type, 0, resp, 0, type.length);
        System.arraycopy(strByte, 0, resp, type.length, strByte.length);
        return Hex.toHexString(resp);
    }
    
    /**
               * 创建payload交易
     *
     * @param nonce
     * @param payload
     * @param value 
     * @return
     */
    public static RawTransaction createPayloadTransaction(BigInteger nonce,String to, String payload,BigInteger value){
    	RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(1000000000L), to, value, Numeric.toHexString(payload.getBytes()));
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
     * decode链上交易(单笔或批量)
     *
     * @param txMsg  未知类型链上交易(单笔或批量)
     * @return 原始交易信息
     */
    public static List<SignedRawTransaction> decodeArbitrayTxMsg(String txMsg) throws IOException {
        List<SignedRawTransaction> rtxs = new LinkedList<>();
        // 单笔交易
        rtxs.add(decodeTxMsg(txMsg));
        return rtxs;
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
    
    /**
     * encode kv交易
     *
     * @param kvtx
     * @return
     */
    public static byte[] encodeWithKV(String key,String value) {
    	List<RlpType> values = asKVRlpValues(key,value);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }
    
    private static List<RlpType> asKVRlpValues(String key,String value) {
    	List<RlpType> result = new ArrayList();
    	result.add(RlpString.create(key));
        result.add(RlpString.create(value));
        return result;
    }
    
    /**
     * encode kv prefix查詢
     *
     * @param prefix
     * @param key
     * @param limit
     * @return
     */
    public static byte[] encodeKVPrefixQuery(String prefix, String lastKey,BigInteger limit) {
    	List<RlpType> values = asKVPrefixValues(prefix,lastKey,limit);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }
    
    private static List<RlpType> asKVPrefixValues(String prefix, String lastKey,BigInteger limit) {
    	List<RlpType> result = new ArrayList();
        result.add(RlpString.create(prefix));
        result.add(RlpString.create(lastKey));
        result.add(RlpString.create(limit));
        return result;
    }
}
