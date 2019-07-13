package com.rendez.api.blockdb;

import lombok.Data;

import java.math.BigInteger;

@Data
public class BlockDbTransaction {
    private String address;
    private BigInteger timestamp;
    private byte[] value;
    private byte opcode;

    public BlockDbTransaction(String address,BigInteger timestamp,byte[] value,byte opcode){
        this.address = address;
        this.timestamp = timestamp;
        this.value = value;
        this.opcode = opcode;
    }

}
