package annchain.genesis.sdk.abi.datatypes.generated;

import annchain.genesis.sdk.abi.datatypes.Int;

import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.anChain.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/anChain/anChain/tree/master/codegen">codegen module</a> to update.
 */
public class Int208 extends Int {
    public static final Int208 DEFAULT = new Int208(BigInteger.ZERO);

    public Int208(BigInteger value) {
        super(208, value);
    }

    public Int208(long value) {
        this(BigInteger.valueOf(value));
    }
}
