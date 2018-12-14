package annchain.genesis.sdk.core.protocol.core.methods.request;

import annchain.genesis.sdk.abi.datatypes.Type;
import annchain.genesis.sdk.abi.TypeReference;

import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/23
 * Time: 11:03
 */
public class ExecuteContractReq extends BaseRequest{
    private String method_name;
    private String gas_price;
    private String gas_limit;
    private String amount;
    private List<Type> inputArgs;
    private List<TypeReference<?>> outputArgs;

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getGas_price() {
        return gas_price;
    }

    public void setGas_price(String gas_price) {
        this.gas_price = gas_price;
    }

    public String getGas_limit() {
        return gas_limit;
    }

    public void setGas_limit(String gas_limit) {
        this.gas_limit = gas_limit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public List<Type> getInputArgs() {
        return inputArgs;
    }

    public void setInputArgs(List<Type> inputArgs) {
        this.inputArgs = inputArgs;
    }

    public List<TypeReference<?>> getOutputArgs() {
        return outputArgs;
    }

    public void setOutputArgs(List<TypeReference<?>> outputArgs) {
        this.outputArgs = outputArgs;
    }
}
