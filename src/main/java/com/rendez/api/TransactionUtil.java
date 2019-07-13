package com.rendez.api;

import com.rendez.api.blockdb.BlockDbTransaction;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.Signature;
import com.rendez.api.crypto.SignatureECDSA;
import com.rendez.api.crypto.blockdb.SignedBlockDbTransaction;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TransactionUtil {
    /**
     * 创建合约调用交易
     *
     * @param nonce
     * @param contractAddress
     * @param function        函数定义
     * @return
     */
    public static RawTransaction createCallContractTransaction(BigInteger nonce, String contractAddress, Function function) {
        RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(100000000000L), contractAddress, FunctionEncoder.encode(function));
        return rtx;
    }

    /**
     * 创建部署合约交易
     *
     * @param binaryCode            二进制合约代码
     * @param constructorParameters 构建参数
     * @param nonce
     * @return
     */
    public static RawTransaction createDelopyContractTransaction(String binaryCode, List<Type> constructorParameters, BigInteger nonce) {
        RawTransaction rtx = RawTransaction.createTransaction(nonce, BigInteger.ZERO, BigInteger.valueOf(100000000000L), null, binaryCode + FunctionEncoder.encodeConstructor(constructorParameters));
        return rtx;
    }

    /**
     * decode单笔链上交易
     *
     * @param txMsg 链上交易
     * @return 原始交易信息
     */
    public static SignedBlockDbTransaction decodeTxMsg(String txMsg) {
        SignedBlockDbTransaction signedRawTransaction = (SignedBlockDbTransaction) com.rendez.api.crypto.blockdb.TransactionDecoder.decode(txMsg);
        return signedRawTransaction;
    }

    public static byte[] encode(BlockDbTransaction rawTransaction, byte chainId) {
        Sign.SignatureData signatureData = new Sign.SignatureData(chainId, new byte[0], new byte[0]);
        return encodeWithSig(rawTransaction, new SignatureECDSA(signatureData));
    }

    public static Sign.SignatureData createEip155SignatureData(Sign.SignatureData signatureData, byte chainId) {
        byte v = (byte) (signatureData.getV() + (chainId << 1) + 8);
        return new Sign.SignatureData(v, signatureData.getR(), signatureData.getS());
    }


    /**
     * encode交易
     *
     * @param tx
     * @return
     */
    public static byte[] encode(BlockDbTransaction tx) {
        return encodeWithSig(tx, null);
    }

    /**
     * encode原始交易和签名
     *
     * @param tx
     * @return
     */
    public static byte[] encode(BlockDbTransaction tx, PrivateKey privateKey) {
        Signature signature = CryptoUtil.generateSignature(tx, privateKey);
        return encodeWithSig(tx, signature);
    }


    /**
     * 使用已生成的签名encode交易
     *
     * @param tx
     * @return
     */
    public static byte[] encodeWithSig(BlockDbTransaction tx, Signature sig) {
        List<RlpType> values = asRlpValues(tx, sig);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    private static List<RlpType> asRlpValues(BlockDbTransaction rawTransaction, Signature sig) {
//        List<RlpType> transactionRLP = new ArrayList();
        List<RlpType> result = new ArrayList();
        result.add(RlpString.create(rawTransaction.getAddress()));
        result.add(RlpString.create(rawTransaction.getTimestamp()));
        result.add(RlpString.create(rawTransaction.getValue()));
        result.add(RlpString.create(rawTransaction.getOpcode()));

        if (sig != null) {
            result.add(RlpString.create(sig.getV()));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(sig.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(sig.getS())));
        }
//        transactionRLP.addAll(result);
        return result;
    }

}
