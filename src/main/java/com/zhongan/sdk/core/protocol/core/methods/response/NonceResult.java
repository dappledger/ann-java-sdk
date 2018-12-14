package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

import java.math.BigInteger;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 10:22
 */
public class NonceResult extends Response<Integer> {

    public Integer getNonce(){
        return getResult();
    }
}
