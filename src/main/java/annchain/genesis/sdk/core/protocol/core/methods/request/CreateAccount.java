package annchain.genesis.sdk.core.protocol.core.methods.request;


public class CreateAccount extends BaseRequest{

    private String starting_balance;

    public String getStartingBalance() {
        return starting_balance;
    }

    public void setStartingBalance(String startingBalance) {
        this.starting_balance = startingBalance;
    }

    @Override
    public String toString() {
        return "{" +
                "\"starting_balance\":\"" + starting_balance + '\"' +
                '}';
    }
}
