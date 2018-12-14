package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_protocolVersion.
 */
public class GenProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
