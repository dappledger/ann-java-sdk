package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.utils.utils.Numeric;
import annchain.genesis.sdk.core.protocol.core.Response;

import java.math.BigInteger;

/**
 * eth_getBalance.
 */
public class GenGetBalance extends Response<String> {
    public BigInteger getBalance() {
        return Numeric.decodeQuantity(getResult());
    }
}
