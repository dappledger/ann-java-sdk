package com.genesis.api.crypto;


public interface PrivateKey {

    byte[] toBytes();
    Signature sign(byte[] msg);
    PublicKey getPubKey();
    String toHexString();
    String getCryptoType();
    String getAddress();
}
