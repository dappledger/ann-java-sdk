package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;
import annchain.genesis.sdk.utils.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_getUncleCountByBlockHash.
 */
public class GenGetUncleCountByBlockHash extends Response<String> {
    public BigInteger getUncleCount() {
        return Numeric.decodeQuantity(getResult());
    }
}
