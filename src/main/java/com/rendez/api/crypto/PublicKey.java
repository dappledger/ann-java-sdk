package com.rendez.api.crypto;


public interface PublicKey {
    String getAddress();
    byte[] toBytes();
    String toHexString();
    boolean verifyBytes(byte[]msg, Signature sig);
}
