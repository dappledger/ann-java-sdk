package annchain.genesis.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 9:58
 */
public class QueryAccountReq extends OpType{

    private String Account;

    public QueryAccountReq(String account) {
        Account = account;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }
}
