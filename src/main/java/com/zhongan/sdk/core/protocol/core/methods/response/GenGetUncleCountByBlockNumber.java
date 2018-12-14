package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;
import com.zhongan.sdk.utils.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_getUncleCountByBlockNumber.
 */
public class GenGetUncleCountByBlockNumber extends Response<String> {
    public BigInteger getUncleCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
