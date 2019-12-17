package com.genesis.api.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QueryPrefixKVs {
	private TransactionKVTx[] txs;
    private String errMsg;
}