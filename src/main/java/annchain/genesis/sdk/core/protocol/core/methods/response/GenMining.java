package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_mining.
 */
public class GenMining extends Response<Boolean> {
    public boolean isMining() {
        return getResult();
    }
}
