package annchain.genesis.sdk.core.protocol.core.methods.request;

public class QueryLedger extends Pagination {

    private Long height;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }
}
