package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;
import annchain.genesis.sdk.utils.utils.Numeric;

import java.math.BigInteger;

/**
 * eth_blockNumber.
 */
public class GenBlockNumber extends Response<String> {
    public BigInteger getBlockNumber() {
        return Numeric.decodeQuantity(getResult());
    }
}
