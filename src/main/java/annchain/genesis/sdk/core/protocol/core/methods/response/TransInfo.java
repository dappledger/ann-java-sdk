package annchain.genesis.sdk.core.protocol.core.methods.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TransInfo{

    private String amount;
    @JsonProperty("created_at")
    private String createdAt;
    private Long basefee;
    private String from;
    private String hash;
    private Long height;
    private Long id;
    private Long nonce;
    private String memo;
    private String to;
    private String optype;
    @JsonProperty("type_i")
    private Long typeI;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public Long getBasefee() {
        return basefee;
    }

    public void setBasefee(Long basefee) {
        this.basefee = basefee;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTypeI() {
        return typeI;
    }

    public void setTypeI(Long typeI) {
        this.typeI = typeI;
    }
}
