package com.zhongan.sdk.core.utils;

import java.util.Arrays;
import java.util.Locale;

public class ByteUtilities {

  /**
   * Remove leading 0x00's from a byte array.
   */
  public static byte[] stripLeadingNullBytes(byte[] input) {
    byte[] result = Arrays.copyOf(input, input.length);
    while (result.length > 1 && result[0] == 0x00) {
      result = Arrays.copyOfRange(result, 1, result.length);
    }
    return result;
  }

  private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

  /**
   * Convert a byte array into its hex string equivalent.
   */
  public static String toHexString(byte[] data) {
    if (data == null) {
      return "";
    }
    char[] chars = new char[data.length * 2];
    for (int i = 0; i < data.length; i++) {
      chars[i * 2] = HEX_DIGITS[(data[i] >> 4) & 0xf];
      chars[i * 2 + 1] = HEX_DIGITS[data[i] & 0xf];
    }
    return new String(chars).toLowerCase(Locale.US);
  }

  /**
   * Convert a hex string into its byte array equivalent.
   */
  public static byte[] toByteArray(String data) {
    if (data == null) {
      return new byte[]{};
    }

    if (data.length() == 0) {
      return new byte[]{};
    }

    while (data.length() < 2) {
      data = "0" + data;
    }

    if (data.substring(0, 2).equalsIgnoreCase("0x")) {
      data = data.substring(2);
    }
    if (data.length() % 2 != 0) {
      data = "0" + data;
    }

    data = data.toUpperCase(Locale.US);

    byte[] bytes = new byte[data.length() / 2];
    String hexString = new String(HEX_DIGITS);
    for (int i = 0; i < bytes.length; i++) {
      int byteConv = hexString.indexOf(data.charAt(i * 2)) * 0x10;
      byteConv += hexString.indexOf(data.charAt(i * 2 + 1));
      bytes[i] = (byte) (byteConv & 0xFF);
    }

    return bytes;
  }

  /**
   * Reverse the endian-ness of a byte array.
   *
   * @param data Byte array to flip
   * @return Flipped array
   */
  public static byte[] flipEndian(byte[] data) {
    if (data == null) {
      return new byte[0];
    }
    byte[] newData = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
      newData[data.length - i - 1] = data[i];
    }

    return newData;
  }

  /**
   * Pad a byte array with leading bytes.
   *
   * @param data Data that needs padding
   * @param size The final desired size of the data.
   * @param pad  The byte value to use in padding the data.
   * @return A padded array.
   */
  public static byte[] leftPad(byte[] data, int size, byte pad) {
    if (size <= data.length) {
      return data;
    }

    byte[] newData = new byte[size];
    for (int i = 0; i < size; i++) {
      newData[i] = pad;
    }
    for (int i = 0; i < data.length; i++) {
      newData[size - i - 1] = data[data.length - i - 1];
    }

    return newData;
  }

  /**
   * Reads a section of a byte array and returns it as its own byte array, not unlike a substring.
   *
   * @param data  Byte array to read from.
   * @param start Starting position of the desired data.
   * @param size  Size of the data.
   * @return Byte array containing the desired data.
   */
  public static byte[] readBytes(byte[] data, int start, int size) {
    if (data.length < start + size) {
      return new byte[0];
    }

    return Arrays.copyOfRange(data, start, start + size);
  }
}
