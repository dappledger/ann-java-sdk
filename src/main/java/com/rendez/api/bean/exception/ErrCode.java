package com.rendez.api.bean.exception;

/**
 * Created by za-zhaogang on 2018/1/13.
 */
public enum ErrCode {

    ERR_BAD_CALL(5001," call chain node error :"),
    ERR_NODEAPI(5002," chain node service error :"),
    ERR_LIBEXCEPTION(5003," 调用库报错："),
    ERR_NO_DATA_FUND(5005," data not fund :");


    private int code;
    private String desc;
    ErrCode(int code,String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getDesc() { return desc; }
    public int getCode(){
        return code;
    }
}
