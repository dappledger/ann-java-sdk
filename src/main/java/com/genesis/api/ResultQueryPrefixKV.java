package com.genesis.api;

import lombok.Data;

@Data
public class ResultQueryPrefixKV {
	private TransactionKVTx[] txs;
    private String errMsg;
}
