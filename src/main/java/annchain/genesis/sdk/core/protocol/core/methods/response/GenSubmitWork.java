package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_submitWork.
 */
public class GenSubmitWork extends Response<Boolean> {

    public boolean solutionValid() {
        return getResult();
    }
}
