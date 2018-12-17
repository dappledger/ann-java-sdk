package annchain.genesis.sdk.core.protocol.core.methods.request;


public class BaseRequest {
    private String basefee;
    private String memo;
    private String from;
    private String to;
    private String nonce;

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
}
