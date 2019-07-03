package com.rendez.api.bean.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.rendez.api.bean.rendez.BaseResp;
import com.rendez.api.util.RandomNameUtil;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@ToString
public class BaseRequest {
    private static AtomicLong nextId = new AtomicLong(0);
    private String jsonrpc = "2.0";
    private String method;
    private Object params;
    private String id;

    public BaseRequest() {
    }

    public BaseRequest(String method, Object params) {
        this.method = method;
        this.params = params;
        this.id= RandomNameUtil.generateName("jsonrpc-client-",8);
    }



}
