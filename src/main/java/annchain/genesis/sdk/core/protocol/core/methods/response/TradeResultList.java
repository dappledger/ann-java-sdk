package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.List;

public class TradeResultList extends Response<List<TradeInfo>> {

    public List<TradeInfo> getTransInfos(){
        return getResult();
    }
}
