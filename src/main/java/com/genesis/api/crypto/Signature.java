package com.genesis.api.crypto;

public interface Signature {
    byte[] toBytes();

    byte[] getV();
    byte[] getR();
    byte[] getS();
}
