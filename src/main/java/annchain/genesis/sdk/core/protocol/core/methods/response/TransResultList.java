package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 14:26
 */
public class TransResultList extends Response<List<TransInfo>> {

    public List<TransInfo> getTransInfos(){
        return getResult();
    }
}
