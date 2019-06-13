package com.rendez.api.util;

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
}
