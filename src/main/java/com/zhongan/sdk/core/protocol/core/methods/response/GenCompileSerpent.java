package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_compileSerpent.
 */
public class GenCompileSerpent extends Response<String> {
    public String getCompiledSourceCode() {
        return getResult();
    }
}
