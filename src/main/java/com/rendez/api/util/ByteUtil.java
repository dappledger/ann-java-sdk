package com.rendez.api.util;

import com.rendez.api.bean.exception.CryptoException;

import java.math.BigInteger;

public class ByteUtil {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * (bigInteger转bytes时, 首位0是符号位， 表示这个数解析为正数, 这里去掉0)
     * @return
     */
    public static byte[] BigIntToRawBytes(BigInteger num){
        byte[] array = num.toByteArray();
        if (array[0] == 0) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        return array;
    }


    public static String bytesToHex(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int a = 0;
        int index = 0;
        for(byte b : bytes) {
            if(b < 0) {
                a = 256 + b;
            } else {
                a = b;
            }

            buf[index++] = HEX_CHAR[a / 16];
            buf[index++] = HEX_CHAR[a % 16];
        }

        return new String(buf);
    }

    //把一个byte数组转换成另一个长度折半的数组
    public static int[] Decode( byte[] src){
        if (src.length %2 == 1) {
            throw new CryptoException("address byte transfer error ！");
        }
        int[] dst = new int[src.length %2];

        for (int i = 0; i < src.length/2; i++) {
            int a = fromHexChar(src[i*2]);

            int b = fromHexChar(src[i*2+1]);

            dst[i] = (a << 4) | b;
        }

        return dst;
    }

    // fromHexChar converts a hex character into its value and a success flag.
    private static int fromHexChar(byte ch){
            if('0' <= ch && ch <= '9'){
                return ch - '0';
            }else if('a' <= ch && ch <= 'f'){
                return ch - 'a' + 10;
            }else if('A' <= ch && ch <= 'F'){
                return ch - 'A' + 10;
            }

        throw new CryptoException("address byte transfer error ！");
    }
}
