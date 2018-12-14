package com.zhongan.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/23
 * Time: 15:42
 */
public class QueryContract extends BaseRequest{
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "{" +
                "\"payload\":\"" + payload + '\"' +
                '}';
    }
}
