package annchain.genesis.sdk.core.protocol.core.methods.request;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 10:44
 */
public class PaginationReq{

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
