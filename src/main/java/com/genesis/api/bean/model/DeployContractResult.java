package com.genesis.api.bean.model;

import lombok.Data;

@Data
public class DeployContractResult {
    private String contractAddr;
    private String txHash;
}
