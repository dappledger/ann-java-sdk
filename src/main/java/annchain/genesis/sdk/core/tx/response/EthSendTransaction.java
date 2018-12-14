package annchain.genesis.sdk.core.tx.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * eth_sendTransaction.
 */
public class EthSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
