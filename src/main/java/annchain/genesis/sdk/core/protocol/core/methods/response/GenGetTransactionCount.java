package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.utils.utils.Numeric;
import annchain.genesis.sdk.core.protocol.core.Response;

import java.math.BigInteger;

/**
 * eth_getTransactionCount.
 */
public class GenGetTransactionCount extends Response<String> {
    public BigInteger getTransactionCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
