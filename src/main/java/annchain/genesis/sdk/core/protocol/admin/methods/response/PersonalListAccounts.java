package annchain.genesis.sdk.core.protocol.admin.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.List;

/**
 * personal_listAccounts.
 */
public class PersonalListAccounts extends Response<List<String>> {
    public List<String> getAccountIds() {
        return getResult();
    }
}
