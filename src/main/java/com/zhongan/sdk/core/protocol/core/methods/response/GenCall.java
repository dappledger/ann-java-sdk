package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_call.
 */
public class GenCall extends Response<String> {
    public String getValue() {
        return getResult();
    }
}
