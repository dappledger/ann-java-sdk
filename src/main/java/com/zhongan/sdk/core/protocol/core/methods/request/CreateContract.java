package com.zhongan.sdk.core.protocol.core.methods.request;

import com.zhongan.sdk.abi.datatypes.Type;

import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/22
 * Time: 13:36
 */
public class CreateContract extends BaseRequest{

    private String contractCode;
    private List<Type> constructorTypes;
    private String gas_price;
    private String gas_limit;
    private String amount;

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public List<Type> getConstructorTypes() {
        return constructorTypes;
    }

    public void setConstructorTypes(List<Type> constructorTypes) {
        this.constructorTypes = constructorTypes;
    }

    public String getGas_price() {
        return gas_price;
    }

    public void setGas_price(String gas_price) {
        this.gas_price = gas_price;
    }

    public String getGas_limit() {
        return gas_limit;
    }

    public void setGas_limit(String gas_limit) {
        this.gas_limit = gas_limit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
