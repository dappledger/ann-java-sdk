package com.rendez.api.bean.rendez;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BaseResult {
    private int code;
    private String data;
    private String log;
    private String hash;//该hash为交易所在梅克尔树的根hash

    public boolean isSuccess(){
        return this.code == 0;
    }
}
