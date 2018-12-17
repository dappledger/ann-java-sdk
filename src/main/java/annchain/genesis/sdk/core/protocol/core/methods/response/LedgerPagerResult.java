package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;


public class LedgerPagerResult extends Response<LedgerPaper> {

    public LedgerPaper getLedgerPaper(){
        return getResult();
    }
}
