package com.zhongan.sdk.core.tx.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_sendTransaction.
 */
public class EthSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
