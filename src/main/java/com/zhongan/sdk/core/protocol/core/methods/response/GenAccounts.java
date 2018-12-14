package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

import java.util.List;

/**
 * eth_accounts.
 */
public class GenAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
