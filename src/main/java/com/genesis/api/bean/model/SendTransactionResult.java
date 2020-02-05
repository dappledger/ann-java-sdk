package com.genesis.api.bean.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SendTransactionResult {
	private String txHash;
	private String errMsg;
}
