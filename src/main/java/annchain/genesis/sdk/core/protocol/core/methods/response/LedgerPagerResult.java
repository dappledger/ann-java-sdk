package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 10:45
 */

public class LedgerPagerResult extends Response<LedgerPaper> {

    public LedgerPaper getLedgerPaper(){
        return getResult();
    }
}
