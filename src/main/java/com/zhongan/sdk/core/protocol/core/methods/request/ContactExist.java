package com.zhongan.sdk.core.protocol.core.methods.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactExist{
    @JsonProperty("byte_code")
    private String byteCode;
    @JsonProperty("code_hash")
    private String codeHash;
    @JsonProperty("is_exist")
    private Boolean isExist;

    public String getByteCode() {
        return byteCode;
    }

    public void setByteCode(String byteCode) {
        this.byteCode = byteCode;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }
}
