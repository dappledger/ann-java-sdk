package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;
import annchain.genesis.sdk.utils.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_getBlockTransactionCountByHash.
 */
public class GenGetBlockTransactionCountByHash extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
