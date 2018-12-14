package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_compileSerpent.
 */
public class GenCompileSerpent extends Response<String> {
    public String getCompiledSourceCode() {
        return getResult();
    }
}
