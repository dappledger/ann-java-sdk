package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;
import com.zhongan.sdk.utils.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_hashrate.
 */
public class GenHashrate extends Response<String> {
    public BigInteger getHashrate() {
        return Numeric.decodeQuantity(getResult());
    }
}
