package annchain.genesis.sdk.core.protocol.core.methods.request;


public class Payment extends BaseRequest{

    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "{" +
                "\"amount\":\"" + amount + '\"' +
                '}';
    }
}
