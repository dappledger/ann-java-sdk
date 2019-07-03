package com.rendez.api.crypto.blockdb;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockDbHash {
    private BlockDbHash() {
    }

    public static String sha3(String hexInput) {
        byte[] bytes = Numeric.hexStringToByteArray(hexInput);
        byte[] result = sha3(bytes);
        return Numeric.toHexString(result);
    }

    public static byte[] sha3(byte[] input, int offset, int length) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        kecc.update(input, offset, length);
        return kecc.digest();
    }

    public static byte[] sha3(byte[] input) {
        return sha3(input, 0, input.length);
    }

    public static String sha3String(String utf8String) {
        return Numeric.toHexString(sha3(utf8String.getBytes(StandardCharsets.UTF_8)));
    }

    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException("Couldn't find a SHA-256 provider", var2);
        }
    }
}