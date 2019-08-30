package com.genesis.api.bean.exception;

public class LIBException extends BaseException{

    public LIBException(Exception ex){
        super(ErrCode.ERR_BAD_CALL.getDesc() + ex.getMessage());
    }
}
