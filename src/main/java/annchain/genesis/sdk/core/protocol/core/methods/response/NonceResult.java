package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

public class NonceResult extends Response<Integer> {

    public Integer getNonce(){
        return getResult();
    }
}
