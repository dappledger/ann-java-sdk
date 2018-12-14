package annchain.genesis.sdk.core.tx.response;

import annchain.genesis.sdk.core.protocol.core.Genereum;
import annchain.genesis.sdk.core.protocol.core.methods.response.TransactionReceipt;
import annchain.genesis.sdk.core.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Optional;

/**
 * Abstraction for managing how we wait for transaction receipts to be generated on the network.
 */
public abstract class TransactionReceiptProcessor {

    private final Genereum genereum;

    public TransactionReceiptProcessor(Genereum genereum) {
        this.genereum = genereum;
    }

    public abstract TransactionReceipt waitForTransactionReceipt(
            String transactionHash)
            throws IOException, TransactionException;

    Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws IOException, TransactionException {
        /*EthGetTransactionReceipt transactionReceipt =
                genereum.ethGetTransactionReceipt(transactionHash).send();*/
        EthGetTransactionReceipt transactionReceipt = new EthGetTransactionReceipt();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
