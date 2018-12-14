package com.zhongan.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 15:09
 */
public class QueryLedgerReq extends PaginationReq {

    private Long height;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }
}
