package com.rendez.api.blockdb;

import lombok.Data;

import java.math.BigInteger;

@Data
public class BlockDbTransaction {
    private String address;
    private BigInteger timestamp;
    private byte[] value;

    public BlockDbTransaction(String address,BigInteger timestamp,byte[] value){
        this.address = address;
        this.timestamp = timestamp;
        this.value = value;
    }

}
