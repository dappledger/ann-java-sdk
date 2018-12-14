package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.List;

/**
 * eth_getCompilers.
 */
public class GenGetCompilers extends Response<List<String>> {
    public List<String> getCompilers() {
        return getResult();
    }
}
