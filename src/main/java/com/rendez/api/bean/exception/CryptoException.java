package com.rendez.api.bean.exception;

import lombok.Data;

@Data
public class CryptoException extends RuntimeException {

    private Exception wrapped;
    private int code;
    private String message;


    public CryptoException(Exception wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    public CryptoException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CryptoException(String message) {
        super(message);
        this.message = message;
    }

    public Exception getWrappedException() {
        return wrapped;
    }
}
