package com.genesis.api;

import lombok.Data;

@Data
public class TransactionNewResult {
	private String txHash;
	private String errMsg;
}
