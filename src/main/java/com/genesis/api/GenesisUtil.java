package com.genesis.api;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * 众安链一些编码和工具的java实现
 * 参考 众安链：rendezvous
 */
public class GenesisUtil {


    public static long DynamicBytesToLong(byte[] bs){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        for (int i= 0; i< (Long.BYTES- bs.length);i++){
            buffer.put((byte)0);
        }
        buffer.put(bs,0, bs.length );
        buffer.flip();//need flip
        return buffer.getLong();
    }


    public static int DynamicBytesToInt(byte[] bs){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        for (int i= 0; i< (Integer.BYTES- bs.length);i++){
            buffer.put((byte)0);
        }
        buffer.put(bs,0, bs.length );
        buffer.flip();//need flip
        return buffer.getInt();
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
            // 转为unsigned int
            byte[] tmp = new byte[lenBytes.length - 1];
            System.arraycopy(lenBytes, 1, tmp, 0, tmp.length);
            lenBytes = tmp;
        }
        byte[] slice = Arrays.copyOfRange(lenBytes, lenBytes.length - lenSize, lenBytes.length); // 去掉空的0字节，只保留有效数字
        output.write(lenSize);
        output.write(slice);
        output.write(data);
    }

    /**
     * 从inputstream中根据data长度的字节表示读取出data
     * 参考 众安链：rendezvous.serialtool.go
     * @param input
     * @throws IOException
     */
    public static byte[] readFromInputStream(ByteArrayInputStream input) throws IOException{
        int dataLenSize = input.read(); // data长度占几个字节
        byte[] dataLenByteSlice = new byte[dataLenSize]; // data长度的字节表示
        input.read(dataLenByteSlice);
        int txLen = DynamicBytesToInt(dataLenByteSlice);
        byte[] data = new byte[txLen];  // data
        input.read(data);
        return data;
    }


}
