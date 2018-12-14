package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.utils.utils.Numeric;
import annchain.genesis.sdk.core.protocol.core.Response;

import java.math.BigInteger;

/**
 * eth_estimateGas.
 */
public class GenEstimateGas extends Response<String> {
    public BigInteger getAmountUsed() {
        return Numeric.decodeQuantity(getResult());
    }
}
