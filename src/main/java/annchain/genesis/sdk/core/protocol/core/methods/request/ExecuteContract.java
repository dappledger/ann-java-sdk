package annchain.genesis.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/13
 * Time: 18:19
 */
public class ExecuteContract extends BaseRequest{

    private String payload;
    private String gas_price;
    private String gas_limit;
    private String amount;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getGas_price() {
        return gas_price;
    }

    public void setGas_price(String gas_price) {
        this.gas_price = gas_price;
    }

    public String getGas_limit() {
        return gas_limit;
    }

    public void setGas_limit(String gas_limit) {
        this.gas_limit = gas_limit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {

        return "{" +
                "\"payload\":\"" + payload + '\"' + ","+
                "\"gas_price\":\"" + gas_price + '\"' + ","+
                "\"gas_limit\":\"" + gas_limit + '\"' + ","+
                "\"amount\":\"" + amount + '\"' +
                '}';
    }
}
