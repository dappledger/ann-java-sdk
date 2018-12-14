package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.utils.utils.Numeric;
import annchain.genesis.sdk.core.protocol.core.Response;

import java.math.BigInteger;

/**
 * eth_hashrate.
 */
public class GenHashrate extends Response<String> {
    public BigInteger getHashrate() {
        return Numeric.decodeQuantity(getResult());
    }
}
