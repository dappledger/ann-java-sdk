package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * eth_protocolVersion.
 */
public class GenProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
