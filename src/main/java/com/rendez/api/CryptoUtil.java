package com.rendez.api;


import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.Signature;
import com.rendez.api.util.ByteUtil;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.SignatureException;

public class CryptoUtil {

    /**
     * 生成签名
     * @param rtx 原始交易
     * @param privateKey 私钥
     * @return 签名
     */
    public static Signature generateSignature(RawTransaction rtx, PrivateKey privateKey){
        return privateKey.sign(TransactionUtil.encode(rtx));
    }

    /**
     * 验证交易是否签名有效
     * @param txMsg
     * @param address
     * @return bool 签名和地址是否一致
     * @throws SignatureException
     */
    public static boolean verifySignature(String txMsg, String address) throws SignatureException {
        SignedRawTransaction signedRawTransaction = (SignedRawTransaction) TransactionDecoder.decode(txMsg);
        String signerAddress = signedRawTransaction.getFrom();
        return signerAddress.equals(address);
    }


    /**
     * 原始签名封装成web3j签名
     * @param v
     * @param r
     * @param s
     * @return
     */
    public static Sign.SignatureData newSignature(byte v, byte[] r, byte[] s){
        return  new Sign.SignatureData(v, r, s);
    }


    /**
     * 生成签名
     * @param rtx 原始交易
     * @param credentials 秘钥
     * @return 签名
     */
    public static Sign.SignatureData generateSignature(RawTransaction rtx, Credentials credentials){
        return Sign.signMessage(TransactionUtil.encode(rtx), credentials.getEcKeyPair());
    }


    /**
     * 生成交易哈希
     * @param message 交易信息
     * @return txHash
     */
    public static String txHash(byte[] message){
        String txHash = Numeric.toHexString(Hash.sha3((message)));
        return txHash;
    }


    /**
     * note: 修改jtendermint.go-wire, varint作为varuint处理
     * @param inputbytes
     * @return
     */
    private static byte[] wireWithVarUint(byte[] inputbytes) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Throwable var2 = null;

            byte[] var9;
            try {
                if (inputbytes == null) {
                    inputbytes = new byte[0];
                }

                long length = inputbytes.length;
                BigInteger bt = BigInteger.valueOf(length);
                byte[] varint = ByteUtil.BigIntToRawBytes(bt);
                long varintLength = (long)varint.length;
                byte[] varintPrefix = ByteUtil.BigIntToRawBytes(BigInteger.valueOf(varintLength));
                bos.write(varintPrefix);
                if (inputbytes.length > 0) {
                    bos.write(varint);
                    bos.write(inputbytes);
                }

                var9 = bos.toByteArray();
            } catch (Throwable var19) {
                var2 = var19;
                throw var19;
            } finally {
                if (bos != null) {
                    if (var2 != null) {
                        try {
                            bos.close();
                        } catch (Throwable var18) {
                            var2.addSuppressed(var18);
                        }
                    } else {
                        bos.close();
                    }
                }

            }

            return var9;
        } catch (Exception var21) {
            return new byte[0];
        }
    }

}
