package com.genesis.api;

import lombok.Data;

@Data
public class TransactionKVResult {
	private String txHash;
	private String errMsg;
}
