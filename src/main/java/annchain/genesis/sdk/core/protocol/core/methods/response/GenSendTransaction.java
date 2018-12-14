package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_sendTransaction.
 */
public class GenSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
