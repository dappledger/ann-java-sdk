package annchain.genesis.sdk.core.tx;

import annchain.genesis.sdk.core.protocol.core.Genereum;
import annchain.genesis.sdk.core.protocol.core.methods.response.TransactionReceipt;
import annchain.genesis.sdk.core.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.math.BigInteger;


/**
 * Generic transaction manager.
 */
public abstract class ManagedTransaction {

    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);

    protected Genereum genereum;

    protected TransactionManager transactionManager;


    protected ManagedTransaction(Genereum genereum, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.genereum = genereum;
    }

    /**
     * Return the current gas price from the ethereum node.
     * <p>
     *     Note: this method was previously called {@code getGasPrice} but was renamed to
     *     distinguish it when a bean accessor method on {@link Contract} was added with that name.
     *     If you have a Contract subclass that is calling this method (unlikely since those
     *     classes are usually generated and until very recently those generated subclasses were
     *     marked {@code final}), then you will need to change your code to call this method
     *     instead, if you want the dynamic behavior.
     * </p>
     * @return the current gas price, determined dynamically at invocation
     * @throws IOException if there's a problem communicating with the ethereum node
     */

    protected TransactionReceipt send(
            String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws IOException, TransactionException {

        return transactionManager.executeTransaction(
                gasPrice, gasLimit, to, data, value);
    }
}
