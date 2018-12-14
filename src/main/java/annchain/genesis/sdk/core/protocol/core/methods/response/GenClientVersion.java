package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * web3_clientVersion.
 */
public class GenClientVersion extends Response<String> {

    public String getGenClientVersion() {
        return getResult();
    }
}
