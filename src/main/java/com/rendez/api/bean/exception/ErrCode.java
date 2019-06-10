package com.rendez.api.bean.exception;

/**
 * Created by za-zhaogang on 2018/1/13.
 */
public enum ErrCode {

    ERR_BAD_CALL("5001, 调用SDK出错:"),
    ERR_NODEAPI("5002, 调用节点错误:"),
    ERR_LIBEXCEPTION("5003, 调用库报错：");



    private final String desc;
    ErrCode(String desc) {
        this.desc = desc;
    }
    public String getDesc() { return desc; }
}
