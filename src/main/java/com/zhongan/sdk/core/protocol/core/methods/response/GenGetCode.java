package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_getCode.
 */
public class GenGetCode extends Response<String> {
    public String getCode() {
        return getResult();
    }
}
