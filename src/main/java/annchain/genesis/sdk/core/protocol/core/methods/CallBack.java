package annchain.genesis.sdk.core.protocol.core.methods;


import annchain.genesis.sdk.abi.datatypes.Type;

import java.util.List;

public interface CallBack {
    void eventCall(String eventName,List<Type> values);
}
