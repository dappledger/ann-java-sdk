package annchain.genesis.sdk.core.tx;

import annchain.genesis.sdk.abi.*;
import annchain.genesis.sdk.abi.datatypes.Address;
import annchain.genesis.sdk.abi.datatypes.Type;
import annchain.genesis.sdk.core.protocol.AnChain;
import annchain.genesis.sdk.core.protocol.core.Genereum;
import annchain.genesis.sdk.core.protocol.core.methods.response.Log;
import annchain.genesis.sdk.abi.*;
import annchain.genesis.sdk.abi.datatypes.Event;
import annchain.genesis.sdk.abi.datatypes.Function;
import annchain.genesis.sdk.core.protocol.core.RemoteCall;
import annchain.genesis.sdk.core.protocol.core.methods.response.GenCall;
import annchain.genesis.sdk.core.protocol.core.methods.response.TransactionReceipt;
import annchain.genesis.sdk.core.protocol.exceptions.TransactionException;
import annchain.genesis.sdk.core.tx.exceptions.ContractCallException;
import annchain.genesis.sdk.crypto.Credentials;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Solidity contract type abstraction for interacting with smart contracts via native Java types.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Contract extends ManagedTransaction {

    // https://www.reddit.com/r/ethereum/comments/5g8ia6/attention_miners_we_recommend_raising_gas_limit/
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);

    protected final String contractBinary;
    protected String contractAddress;
    protected BigInteger gasPrice;
    protected BigInteger gasLimit;
    protected TransactionReceipt transactionReceipt;
    protected Map<String, String> deployedAddresses;

    protected Contract(String contractBinary, String contractAddress,
                       Genereum genereum, TransactionManager transactionManager,
                       BigInteger gasPrice, BigInteger gasLimit) {
        super(genereum, transactionManager);

        this.contractAddress = contractAddress;

        this.contractBinary = contractBinary;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }

    protected Contract(String contractBinary, String contractAddress,
                       Genereum genereum, Credentials credentials,
                       BigInteger gasPrice, BigInteger gasLimit) {
        this(contractBinary, contractAddress, genereum, new RawTransactionManager(genereum, credentials),
                gasPrice, gasLimit);
    }

    @Deprecated
    protected Contract(String contractAddress,
                       Genereum genereum, TransactionManager transactionManager,
                       BigInteger gasPrice, BigInteger gasLimit) {
        this("", contractAddress, genereum, transactionManager, gasPrice, gasLimit);
    }

    @Deprecated
    protected Contract(String contractAddress,
                       Genereum genereum, Credentials credentials,
                       BigInteger gasPrice, BigInteger gasLimit) {
        this("", contractAddress, genereum, new RawTransactionManager(genereum, credentials),
                gasPrice, gasLimit);
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setTransactionReceipt(TransactionReceipt transactionReceipt) {
        this.transactionReceipt = transactionReceipt;
    }

    public String getContractBinary() {
        return contractBinary;
    }
    
    /**
     * Allow {@code gasPrice} to be set.
     * @param newPrice gas price to use for subsequent transactions
     */
    public void setGasPrice(BigInteger newPrice) {
        this.gasPrice = newPrice;
    }

    /**
     * Get the current {@code gasPrice} value this contract uses when executing transactions.
     * @return the gas price set on this contract
     */
    public BigInteger getGasPrice() {
        return gasPrice;
    }

    /**
     * If this Contract instance was created at deployment, the TransactionReceipt associated
     * with the initial creation will be provided, e.g. via a <em>deploy</em> method. This will
     * not persist for Contracts instances constructed via a <em>load</em> method.
     *
     * @return the TransactionReceipt generated at contract deployment
     */
    public Optional<TransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(transactionReceipt);
    }

    /**
     * Execute constant function call - i.e. a call that does not change state of the contract
     *
     * @param function to call
     * @return {@link List} of values returned by function call
     */
    private List<Type> executeCall(
            Function function) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);
       /* GenCall ethCall = genereum.ethCall(
                Transaction.createEthCallTransaction(
                        transactionManager.getFromAddress(), contractAddress, encodedFunction),
                DefaultBlockParameterName.LATEST)
                .send();*/
        GenCall ethCall = new GenCall();
        String value = ethCall.getValue();
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }

    @SuppressWarnings("unchecked")
    protected <T extends Type> T executeCallSingleValueReturn(
            Function function) throws IOException {
        List<Type> values = executeCall(function);
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T extends Type, R> R executeCallSingleValueReturn(
            Function function, Class<R> returnType) throws IOException {
        T result = executeCallSingleValueReturn(function);
        if (result == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }

        Object value = result.getValue();
        if (returnType.isAssignableFrom(value.getClass())) {
            return (R) value;
        } else if (result.getClass().equals(Address.class) && returnType.equals(String.class)) {
            return (R) result.toString();  // cast isn't necessary
        } else {
            throw new ContractCallException(
                    "Unable to convert response: " + value
                            + " to expected type: " + returnType.getSimpleName());
        }
    }

    protected List<Type> executeCallMultipleValueReturn(
            Function function) throws IOException {
        return executeCall(function);
    }

    protected TransactionReceipt executeTransaction(
            Function function)
            throws IOException, TransactionException {
        return executeTransaction(function, BigInteger.ZERO);
    }

    private TransactionReceipt executeTransaction(
            Function function, BigInteger weiValue)
            throws IOException, TransactionException {
        return executeTransaction(FunctionEncoder.encode(function), weiValue);
    }

    /**
     * Given the duration required to execute a transaction.
     *
     * @param data  to send in transaction
     * @param weiValue in Wei to send in transaction
     * @return {@link Optional} containing our transaction receipt
     * @throws IOException                 if the call to the node fails
     * @throws TransactionException if the transaction was not mined while waiting
     */
    TransactionReceipt executeTransaction(
            String data, BigInteger weiValue)
            throws TransactionException, IOException {

        return send(contractAddress, data, weiValue, gasPrice, gasLimit);
    }

    protected <T extends Type> RemoteCall<T> executeRemoteCallSingleValueReturn(Function function) {
        return new RemoteCall<>(() -> executeCallSingleValueReturn(function));
    }

    protected <T> RemoteCall<T> executeRemoteCallSingleValueReturn(
            Function function, Class<T> returnType) {
        return new RemoteCall<>(() -> executeCallSingleValueReturn(function, returnType));
    }

    protected RemoteCall<List<Type>> executeRemoteCallMultipleValueReturn(Function function) {
        return new RemoteCall<>(() -> executeCallMultipleValueReturn(function));
    }

    protected RemoteCall<TransactionReceipt> executeRemoteCallTransaction(Function function) {
        return new RemoteCall<>(() -> executeTransaction(function));
    }

    protected RemoteCall<TransactionReceipt> executeRemoteCallTransaction(
            Function function, BigInteger weiValue) {
        return new RemoteCall<>(() -> executeTransaction(function, weiValue));
    }

    private static <T extends Contract> T create(
            T contract, String binary, String encodedConstructor, BigInteger value)
            throws IOException, TransactionException {
        TransactionReceipt transactionReceipt =
                contract.executeTransaction(binary + encodedConstructor, value);

        String contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress == null) {
            throw new RuntimeException("Empty contract address returned");
        }
        contract.setContractAddress(contractAddress);
        contract.setTransactionReceipt(transactionReceipt);

        return contract;
    }

    protected static <T extends Contract> T deploy(
            Class<T> type,
            AnChain anChain, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) throws
            IOException, TransactionException {

        try {
            Constructor<T> constructor = type.getDeclaredConstructor(
                    String.class,
                    AnChain.class, Credentials.class,
                    BigInteger.class, BigInteger.class);
            constructor.setAccessible(true);

            // we want to use null here to ensure that "to" parameter on message is not populated
            T contract = constructor.newInstance(null, anChain, credentials, gasPrice, gasLimit);

            return create(contract, binary, encodedConstructor, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T extends Contract> T deploy(
            Class<T> type,
            AnChain anChain, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value)
            throws IOException, TransactionException {

        try {
            Constructor<T> constructor = type.getDeclaredConstructor(
                    String.class,
                    AnChain.class, TransactionManager.class,
                    BigInteger.class, BigInteger.class);
            constructor.setAccessible(true);

            // we want to use null here to ensure that "to" parameter on message is not populated
            T contract = constructor.newInstance(
                    null, anChain, transactionManager, gasPrice, gasLimit);
            return create(contract, binary, encodedConstructor, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            AnChain anChain, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) {
        return new RemoteCall<>(() -> deploy(
                type, anChain, credentials, gasPrice, gasLimit, binary,
                encodedConstructor, value));
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            AnChain anChain, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor) {
        return deployRemoteCall(
                type, anChain, credentials, gasPrice, gasLimit,
                binary, encodedConstructor, BigInteger.ZERO);
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            AnChain anChain, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor, BigInteger value) {
        return new RemoteCall<>(() -> deploy(
                type, anChain, transactionManager, gasPrice, gasLimit, binary,
                encodedConstructor, value));
    }

    protected static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            AnChain anChain, TransactionManager transactionManager,
            BigInteger gasPrice, BigInteger gasLimit,
            String binary, String encodedConstructor) {
        return deployRemoteCall(
                type, anChain, transactionManager, gasPrice, gasLimit, binary,
                encodedConstructor, BigInteger.ZERO);
    }

    public static EventValues staticExtractEventParameters(
            Event event, Log log) {

        List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (!topics.get(0).equals(encodedEventSignature)) {
            return null;
        }

        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
                log.getData(), event.getNonIndexedParameters());

        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }

    protected EventValues extractEventParameters(Event event, Log log) {
        return staticExtractEventParameters(event, log);
    }

    protected List<EventValues> extractEventParameters(
            Event event, TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParameters(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected EventValuesWithLog extractEventParametersWithLog(Event event, Log log) {
        final EventValues eventValues = staticExtractEventParameters(event, log);
        return (eventValues == null) ? null : new EventValuesWithLog(eventValues, log);
    }

    protected List<EventValuesWithLog> extractEventParametersWithLog(
            Event event, TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParametersWithLog(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Subclasses should implement this method to return pre-existing addresses for deployed
     * contracts.
     *
     * @param networkId the network id, for example "1" for the main-net, "3" for ropsten, etc.
     * @return the deployed address of the contract, if known, and null otherwise.
     */
    protected String getStaticDeployedAddress(String networkId) {
        return null;
    }

    public final void setDeployedAddress(String networkId, String address) {
        if (deployedAddresses == null) {
            deployedAddresses = new HashMap<>();
        }
        deployedAddresses.put(networkId, address);
    }

    public final String getDeployedAddress(String networkId) {
        String addr = null;
        if (deployedAddresses != null) {
            addr = deployedAddresses.get(networkId);
        }
        return addr == null ? getStaticDeployedAddress(networkId) : addr;
    }

    /**
     * Adds a log field to {@link EventValues}.
     */
    public static class EventValuesWithLog {
        private final EventValues eventValues;
        private final Log log;

        private EventValuesWithLog(EventValues eventValues, Log log) {
            this.eventValues = eventValues;
            this.log = log;
        }

        public List<Type> getIndexedValues() {
            return eventValues.getIndexedValues();
        }

        public List<Type> getNonIndexedValues() {
            return eventValues.getNonIndexedValues();
        }

        public Log getLog() {
            return log;
        }
    }

    @SuppressWarnings("unchecked")
    protected static <S extends Type, T>
            List<T> convertToNative(List<S> arr) {
        List<T> out = new ArrayList<T>();
        for (Iterator<S> it = arr.iterator(); it.hasNext(); ) {
            out.add((T)it.next().getValue());
        }
        return out;
    }
}
