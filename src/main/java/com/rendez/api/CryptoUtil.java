package com.rendez.api;


import com.github.jtendermint.crypto.RipeMD160;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

public class CryptoUtil {


    /**
     * 生成私钥
     * @return privKey 私钥
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static String generatePrivateKey() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException{
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        BigInteger privateKeyInDec = ecKeyPair.getPrivateKey();
        String privKey = privateKeyInDec.toString(16);
        return "0x" + privKey;
    }


    /**
     * 生成账户地址
     * @param privKey
     * @return address 地址
     */
    public static String addressFromPrivkey(String privKey){
        Credentials credentials = Credentials.create(privKey);
        return credentials.getAddress();
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
     * @param message
     * @return txHash
     */
    public static String txHash(byte[] message){
        byte[] encode = wireWithVarUint(message);
        String txHash = Numeric.toHexString(Hash.sha3((encode)));
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
                byte[] varint = bt.toByteArray();
                if (varint[0] == 0) {
                    byte[] tmp = new byte[varint.length - 1];
                    System.arraycopy(varint, 1, tmp, 0, tmp.length);
                    varint = tmp;
                }
                long varintLength = (long)varint.length;
                byte[] varintPrefix = BigInteger.valueOf(varintLength).toByteArray();
                if (varintPrefix[0] == 0) {
                    byte[] tmp = new byte[varintPrefix.length - 1];
                    System.arraycopy(varintPrefix, 1, tmp, 0, tmp.length);
                    varintPrefix = tmp;
                }
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
