package com.zhongan.sdk.core.protocol.admin.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * Boolean response type.
 */
public class BooleanResponse extends Response<Boolean> {
    public boolean success() {
        return getResult();
    }
}
