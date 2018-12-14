package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_sendRawTransaction.
 */
public class GenSendRawTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
