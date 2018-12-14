package annchain.genesis.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 15:12
 */
public class QueryTransTxReq extends OpType{

    private String TxHash;

    public String getTxHash() {
        return TxHash;
    }

    public void setTxHash(String txHash) {
        TxHash = txHash;
    }
}
