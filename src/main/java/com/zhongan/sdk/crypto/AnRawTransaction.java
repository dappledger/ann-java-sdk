package com.zhongan.sdk.crypto;


public class AnRawTransaction {

    private String basefee;
    private String memo;
    private String from;
    private String to;
    private String nonce;
    private String optype;
    private String operation;
    private String signature;
    private String txHash;
    private String rawTransaction;
    public AnRawTransaction(String basefee, String memo, String from, String to, String nonce, String optype, String operation) {
        this.basefee = basefee;
        this.memo = memo;
        this.from = from;
        this.to = to;
        this.nonce = nonce;
        this.optype = optype;
        this.operation = operation;
    }

    public String getBasefee() {
        return basefee;
    }

    public void setBasefee(String basefee) {
        this.basefee = basefee;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getRawTransaction() {
        return rawTransaction;
    }

    public void setRawTransaction(String rawTransaction) {
        this.rawTransaction = rawTransaction;
    }
}
