package com.rendez.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 众安链一些编码和工具的java实现
 * 参考 众安链：rendezvous
 */
public class RendezUtil {

    public static final byte[] BATCH_TAG_PREFIX = "seri".getBytes();
    public static final int  MAX_BATCH_SIZE = 500;



    public static long BytesToLong(byte[] bs){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        for (int i= 0; i< (Long.BYTES- bs.length);i++){
            buffer.put((byte)0);
        }
        buffer.put(bs,0, bs.length );
        buffer.flip();//need flip
        return buffer.getLong();
    }
    /**
     * int转为4字节表示
     * @param i
     * @return
     */
    public static byte[] ToInt32Bytes(int i){
        return ByteBuffer.allocate(4).putInt(i).array();
    }

    /**
     * int包含几个有效字节
     * @param i
     * @return
     */
    public static int varIntSize(int i){

        if (i == 0){
            return 0;
        }
        if (i < 1<<8){
            return 1;
        }

        if (i < 1<<16){
            return 2;
        }
        if (i < 1<<24){
            return 3;
        }
        return 4;
    }


    /**
     * 将data字节数组的长度有效字节长度, 长度有效字节, data依次写入outputStream
     * 参考 众安链：rendezvous.serialtool.go
     * @param output
     * @param data
     * @throws IOException
     */
    public static void writeToStream(ByteArrayOutputStream output, byte[] data) throws IOException{
        int len = data.length;
        int lenSize = varIntSize(len); // len实际占用几个字节
        byte[] lenBytes = BigInteger.valueOf(len).toByteArray(); // // len的字节表示
        if (lenBytes[0] == 0) {
            byte[] tmp = new byte[lenBytes.length - 1]; // 转为unsigned int
            System.arraycopy(lenBytes, 1, tmp, 0, tmp.length);
            lenBytes = tmp;
        }
        byte[] slice = Arrays.copyOfRange(lenBytes, lenBytes.length - lenSize, lenBytes.length); // 去掉空的0字节，只保留有效数字
        output.write(lenSize);
        output.write(slice);
        output.write(data);
    }

}
