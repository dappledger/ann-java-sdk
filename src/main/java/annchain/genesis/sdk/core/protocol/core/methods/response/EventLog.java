package annchain.genesis.sdk.core.protocol.core.methods.response;


import java.util.List;

public class EventLog {
    private String address;
    private List<String> topics;
    private String data;
    private String blockNumber;
    private String transactionIndex;
    private String transactionHash;
    private String blockHash;
    private String logIndex;
    private boolean removed;

    public EventLog() {
    }

    public EventLog(String address, List<String> topics, String data, String blockNumber, String transactionIndex, String transactionHash, String blockHash, String logIndex, boolean removed) {
        this.address = address;
        this.topics = topics;
        this.data = data;
        this.blockNumber = blockNumber;
        this.transactionIndex = transactionIndex;
        this.transactionHash = transactionHash;
        this.blockHash = blockHash;
        this.logIndex = logIndex;
        this.removed = removed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getTransactionIndex() {
        return transactionIndex;
    }

    public void setTransactionIndex(String transactionIndex) {
        this.transactionIndex = transactionIndex;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(String logIndex) {
        this.logIndex = logIndex;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
