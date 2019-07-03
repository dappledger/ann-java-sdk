package com.rendez.api.bean.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;

@Data
@ToString
public class BlockDbResult {


//    Height    uint64         `json:"height"`
//    Timestamp uint64         `json:"timestamp"`
//    From      common.Address `json:"from"`
//    Value     []byte         `json:"value"`
//    TxHash    common.Hash    `json:"txhash" gencodec:"required"`
//    Status    uint64         `json:"status"`
    private BigInteger height;
    private BigInteger timestamp;
    private String from;
    private String value;
    private String txhash;
    //private BigInteger status;

}
