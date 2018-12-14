package annchain.genesis.sdk.abi.datatypes.generated;

import annchain.genesis.sdk.abi.datatypes.Uint;

import java.math.BigInteger;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.anChain.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/anChain/anChain/tree/master/codegen">codegen module</a> to update.
 */
public class Uint152 extends Uint {
    public static final Uint152 DEFAULT = new Uint152(BigInteger.ZERO);

    public Uint152(BigInteger value) {
        super(152, value);
    }

    public Uint152(long value) {
        this(BigInteger.valueOf(value));
    }
}
