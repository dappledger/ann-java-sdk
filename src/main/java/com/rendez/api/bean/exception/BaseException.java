package com.rendez.api.bean.exception;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {
    private ErrCode error;

    public BaseException(ErrCode error, String msg){
        super(error.getDesc() + msg);
        this.setError(error);
    }

    public BaseException(String msg) {
        super(ErrCode.ERR_BAD_CALL.getDesc() + msg);
        this.setError(ErrCode.ERR_BAD_CALL);
    }
}
