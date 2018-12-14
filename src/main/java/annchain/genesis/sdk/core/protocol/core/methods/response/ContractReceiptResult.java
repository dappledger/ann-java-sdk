package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;

/**
 * User: za-luguiming
 * Date: 2018/12/5
 * Time: 16:07
 */
public class ContractReceiptResult extends Response<ContractReceipt> {

    public ContractReceipt getReceiptResult(){
        return getResult();
    }

}
