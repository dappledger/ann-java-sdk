package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

public class AccountResult extends Response<AccountVo> {

    public AccountVo getAccount(){
        return getResult();
    }
}
