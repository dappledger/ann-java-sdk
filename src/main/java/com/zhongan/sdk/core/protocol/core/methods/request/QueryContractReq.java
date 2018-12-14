package com.zhongan.sdk.core.protocol.core.methods.request;

import com.zhongan.sdk.abi.TypeReference;
import com.zhongan.sdk.abi.datatypes.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/23
 * Time: 11:03
 */
public class QueryContractReq extends BaseRequest{
    private String method_name;
    private List<Type> inputArgs;
    private List<TypeReference<?>> outputArgs = new ArrayList<>();

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public List<Type> getInputArgs() {
        return inputArgs;
    }

    public void setInputArgs(List<Type> inputArgs) {
        this.inputArgs = inputArgs;
    }

    public List<TypeReference<?>> getOutputArgs() {
        return outputArgs;
    }

    public void setOutputArgs(List<TypeReference<?>> outputArgs) {
        this.outputArgs = outputArgs;
    }
}
