package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.core.protocol.core.methods.request.Value;

import java.util.Map;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 10:03
 */
public class AccountVo {
    private String address;
    private String balance;
    private Map<String,Value> data;

    public AccountVo() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Map<String, Value> getData() {
        return data;
    }

    public void setData(Map<String, Value> data) {
        this.data = data;
    }

    public AccountVo(String address, String balance, Map<String, Value> data) {
        this.address = address;
        this.balance = balance;
        this.data = data;
    }
}
