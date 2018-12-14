package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.Response;

import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 10:45
 */
public class LedgerPagerListResult extends Response<List<LedgerPaper>> {

    public List<LedgerPaper> getLedgerPaper(){
        return getResult();
    }
}
