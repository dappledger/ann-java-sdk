package com.rendez.api.bean.rendez;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResp<T> {

    private String jsonrpc;
    private String id;
    private T result;

    public <T> BaseResp(){

    }


}
