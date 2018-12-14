package com.zhongan.sdk.core.tx;

import com.zhongan.sdk.core.protocol.core.Genereum;
import com.zhongan.sdk.core.protocol.core.methods.response.TransactionReceipt;
import com.zhongan.sdk.core.protocol.exceptions.TransactionException;
import com.zhongan.sdk.core.tx.response.EthSendTransaction;
import com.zhongan.sdk.core.tx.response.PollingTransactionReceiptProcessor;
import com.zhongan.sdk.core.tx.response.TransactionReceiptProcessor;

import java.io.IOException;
import java.math.BigInteger;

import static com.zhongan.sdk.core.protocol.core.JsonRpc2_0AnChain.DEFAULT_BLOCK_TIME;

/**
 * Transaction manager abstraction for executing transactions with Ethereum client via
 * various mechanisms.
 */
public abstract class TransactionManager {

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;

    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String fromAddress;

    protected TransactionManager(
            TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.fromAddress = fromAddress;
    }

    protected TransactionManager(Genereum genereum, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(
                        genereum, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }

    protected TransactionManager(
            Genereum genereum, int attempts, long sleepDuration, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(genereum, sleepDuration, attempts), fromAddress);
    }

    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException, TransactionException {

        EthSendTransaction ethSendTransaction = sendTransaction(
                gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);
    }

    public abstract EthSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException;

    public String getFromAddress() {
        return fromAddress;
    }

    private TransactionReceipt processResponse(EthSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: "
                    + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }


}
