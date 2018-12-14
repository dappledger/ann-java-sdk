package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 14:26
 */
public class TradeResultList extends Response<List<TradeInfo>> {

    public List<TradeInfo> getTransInfos(){
        return getResult();
    }
}
