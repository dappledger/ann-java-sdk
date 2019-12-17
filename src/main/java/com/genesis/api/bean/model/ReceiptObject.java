package com.genesis.api.bean.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ReceiptObject {
    private String PostState;
    private BigInteger CumulativeGasUsed;
    private String Bloom;
    private ReceiptLogs Logs;
    private String TxHash;
    private String ContractAddress;
    private BigInteger GasUsed;
    private BigInteger Height;

}