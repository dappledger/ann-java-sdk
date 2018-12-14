package com.zhongan.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/13
 * Time: 18:10
 */
public class Payment extends BaseRequest{

    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{" +
                "\"amount\":\"" + amount + '\"' +
                '}';
    }
}
