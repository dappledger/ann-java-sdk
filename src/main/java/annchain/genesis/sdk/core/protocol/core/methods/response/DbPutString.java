package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * db_putString.
 */
public class DbPutString extends Response<Boolean> {

    public boolean valueStored() {
        return getResult();
    }
}
