package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_getStorageAt.
 */
public class GenGetStorageAt extends Response<String> {
    public String getData() {
        return getResult();
    }
}
