package annchain.genesis.sdk.core.tx;

import annchain.genesis.sdk.core.protocol.core.Genereum;
import annchain.genesis.sdk.crypto.RawTransaction;
import annchain.genesis.sdk.core.tx.response.EthSendTransaction;
import annchain.genesis.sdk.core.tx.response.TransactionReceiptProcessor;
import annchain.genesis.sdk.crypto.Credentials;
import annchain.genesis.sdk.crypto.TransactionEncoder;
import annchain.genesis.sdk.utils.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;

/**
 * TransactionManager implementation using Ethereum wallet file to create and sign transactions
 * locally.
 *
 * <p>This transaction manager provides support for specifying the chain id for transactions as per
 * <a href="https://github.com/ethereum/EIPs/issues/155">EIP155</a>.
 */
public class RawTransactionManager extends TransactionManager {

    private final Genereum genereum;
    final Credentials credentials;

    private final byte chainId;

    public RawTransactionManager(Genereum genereum, Credentials credentials, byte chainId) {
        super(genereum, credentials.getAddress());

        this.genereum = genereum;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(
            Genereum genereum, Credentials credentials, byte chainId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());

        this.genereum = genereum;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(
            Genereum genereum, Credentials credentials, byte chainId, int attempts, long sleepDuration) {
        super(genereum, attempts, sleepDuration, credentials.getAddress());

        this.genereum = genereum;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(Genereum genereum, Credentials credentials) {
        this(genereum, credentials, ChainId.NONE);
    }

    public RawTransactionManager(
            Genereum genereum, Credentials credentials, int attempts, int sleepDuration) {
        this(genereum, credentials, ChainId.NONE, attempts, sleepDuration);
    }

    protected BigInteger getNonce() throws IOException {
       /* EthGetTransactionCount ethGetTransactionCount = genereum.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();*/

        return null;
    }

    @Override
    public EthSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value) throws IOException {

        BigInteger nonce = getNonce();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        return signAndSend(rawTransaction);
    }

    public EthSendTransaction signAndSend(RawTransaction rawTransaction)
            throws IOException {

        byte[] signedMessage;

        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String hexValue = Numeric.toHexString(signedMessage);

        //return genereum.ethSendRawTransaction(hexValue).send();
        return null;
    }
}
