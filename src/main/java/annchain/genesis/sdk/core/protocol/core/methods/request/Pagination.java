package annchain.genesis.sdk.core.protocol.core.methods.request;


public class Pagination {

    private Integer cursor=0;
    private Integer limit = 10;
    private String order = "desc";

    public Integer getCursor() {
        return cursor;
    }

    public void setCursor(Integer cursor) {
        this.cursor = cursor;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
