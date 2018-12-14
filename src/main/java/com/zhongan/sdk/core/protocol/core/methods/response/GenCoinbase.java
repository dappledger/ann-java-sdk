package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_coinbase.
 */
public class GenCoinbase extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
