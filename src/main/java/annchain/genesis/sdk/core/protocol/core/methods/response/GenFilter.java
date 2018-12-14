package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;
import annchain.genesis.sdk.utils.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_newFilter.
 */
public class GenFilter extends Response<String> {
    public BigInteger getFilterId() {
        return Numeric.decodeQuantity(getResult());
    }
}
