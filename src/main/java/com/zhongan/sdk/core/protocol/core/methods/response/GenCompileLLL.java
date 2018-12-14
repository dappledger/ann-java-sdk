package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_compileLLL.
 */
public class GenCompileLLL extends Response<String> {
    public String getCompiledSourceCode() {
        return getResult();
    }
}
