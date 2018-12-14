package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_sign.
 */
public class GenSign extends Response<String> {
    public String getSignature() {
        return getResult();
    }
}
