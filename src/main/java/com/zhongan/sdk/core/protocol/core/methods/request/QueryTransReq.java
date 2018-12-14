package com.zhongan.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 15:09
 */
public class QueryTransReq extends PaginationReq {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
