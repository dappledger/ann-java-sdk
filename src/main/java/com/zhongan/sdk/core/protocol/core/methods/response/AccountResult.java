package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 10:36
 */
public class AccountResult extends Response<AccountVo> {

    public AccountVo getAccount(){
        return getResult();
    }
}
