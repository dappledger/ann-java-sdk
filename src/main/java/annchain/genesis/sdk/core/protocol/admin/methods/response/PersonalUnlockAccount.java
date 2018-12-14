package annchain.genesis.sdk.core.protocol.admin.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * personal_unlockAccount.
 */
public class PersonalUnlockAccount extends Response<Boolean> {
    public Boolean accountUnlocked() {
        return getResult();
    }
}