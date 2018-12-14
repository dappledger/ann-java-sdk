package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 14:26
 */
public class TradeResult extends Response<List<TradeInfo>> {

    public List<TradeInfo> getTransInfos(){
        return getResult();
    }
}
