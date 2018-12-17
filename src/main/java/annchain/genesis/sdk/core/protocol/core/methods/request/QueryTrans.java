package annchain.genesis.sdk.core.protocol.core.methods.request;


public class QueryTrans extends Pagination {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
