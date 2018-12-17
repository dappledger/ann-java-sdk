package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.List;

public class LedgerPagerListResult extends Response<List<LedgerPaper>> {

    public List<LedgerPaper> getLedgerPaper(){
        return getResult();
    }
}
