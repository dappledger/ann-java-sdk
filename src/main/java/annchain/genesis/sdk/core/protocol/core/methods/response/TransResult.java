package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.List;

public class TransResult extends Response<List<TransInfo>> {

    public List<TransInfo> getTransInfos(){
        return getResult();
    }
}
