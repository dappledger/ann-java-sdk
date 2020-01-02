package com.genesis.api.bean.genesis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResp<T> {

    private String jsonrpc;
    private String id;
    private T result;
    private String error;

    public <T> BaseResp(){

    }
}
