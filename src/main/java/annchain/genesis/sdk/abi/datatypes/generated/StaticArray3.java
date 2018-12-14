package annchain.genesis.sdk.abi.datatypes.generated;

import annchain.genesis.sdk.abi.datatypes.StaticArray;
import annchain.genesis.sdk.abi.datatypes.Type;

import java.util.List;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.anChain.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/anChain/anChain/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray3<T extends Type> extends StaticArray<T> {
    public StaticArray3(List<T> values) {
        super(3, values);
    }

    @SafeVarargs
    public StaticArray3(T... values) {
        super(3, values);
    }
}
