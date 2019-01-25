package annchain.genesis.sdk.core.protocol.core.methods.request;

import annchain.genesis.sdk.abi.datatypes.Type;
import annchain.genesis.sdk.abi.TypeReference;

import java.util.ArrayList;
import java.util.List;


public class GetContractResult extends BaseRequest{
    private String method_name;
    private List<Type> inputArgs;
    private List<TypeReference<Type>> outputArgs = new ArrayList<>();

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public List<Type> getInputArgs() {
        return inputArgs;
    }

    public void setInputArgs(List<Type> inputArgs) {
        this.inputArgs = inputArgs;
    }

    public List<TypeReference<Type>> getOutputArgs() {
        return outputArgs;
    }

    public void setOutputArgs(List<TypeReference<Type>> outputArgs) {
        this.outputArgs = outputArgs;
    }
}
