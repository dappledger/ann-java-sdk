package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

public class ContractReceiptResult extends Response<ContractReceipt> {

    public ContractReceipt getReceiptResult(){
        return getResult();
    }

}
