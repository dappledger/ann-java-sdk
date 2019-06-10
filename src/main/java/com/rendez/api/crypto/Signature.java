package com.rendez.api.crypto;

public interface Signature {
    byte[] toBytes();

    byte[] getV();
    byte[] getR();
    byte[] getS();
}
