package com.genesis.api;

import com.genesis.api.crypto.PrivateKey;
import com.genesis.api.crypto.Signature;
import com.genesis.api.util.ByteUtil;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

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
