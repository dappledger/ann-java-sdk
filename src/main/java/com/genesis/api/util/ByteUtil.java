package com.genesis.api.util;

import java.math.BigInteger;

public class ByteUtil {


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
}
