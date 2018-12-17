package annchain.genesis.sdk.core.protocol.core.methods.request;


public class QueryContract extends BaseRequest{
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "{" +
                "\"payload\":\"" + payload + '\"' +
                '}';
    }
}
