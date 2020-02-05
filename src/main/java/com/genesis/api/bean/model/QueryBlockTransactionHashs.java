package com.genesis.api.bean.model;

import lombok.Data;

@Data
public class QueryBlockTransactionHashs {
	private String[] hashs;
	private long  count;
	
	public QueryBlockTransactionHashs(String[] txhashs, long num){
		hashs = txhashs;
		count = num;
	}
}
