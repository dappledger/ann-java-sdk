package annchain.genesis.sdk.abi.datatypes;

import annchain.genesis.sdk.abi.Utils;
import annchain.genesis.sdk.abi.TypeReference;

import java.util.List;

/**
 * Function type.
 */
public class Function {
    private String name;
    private List<Type> inputParameters;
    private List<TypeReference<Type>> outputParameters;

    public Function(String name, List<Type> inputParameters,
                    List<TypeReference<Type>> outputParameters) {
        this.name = name;
        this.inputParameters = inputParameters;
        this.outputParameters = Utils.convert(outputParameters);
    }

    public String getName() {
        return name;
    }

    public List<Type> getInputParameters() {
        return inputParameters;
    }

    public List<TypeReference<Type>> getOutputParameters() {
        return outputParameters;
    }
}
