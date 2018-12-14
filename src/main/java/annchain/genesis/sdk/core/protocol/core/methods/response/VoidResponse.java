package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * Null response object returned by the below methods.
 * <ul>
 * <li>personal_setAccountName</li>
 * <li>personal_setAccountMeta</li>
 * </ul>
 */
public class VoidResponse extends Response<Void> {
    public boolean isValid() {
        return !hasError();
    }
}
