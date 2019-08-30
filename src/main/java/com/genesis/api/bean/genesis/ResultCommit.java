package com.genesis.api.bean.genesis;

import lombok.Data;

@Data
public class ResultCommit {
    private int Code;
    private String Data;
    private String Log;
    private String tx_hash;

    public boolean isSuccess(){
        return this.Code == 0;
    }
}
