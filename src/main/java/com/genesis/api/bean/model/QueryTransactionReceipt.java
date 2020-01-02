package com.genesis.api.bean.model;

import org.ethereum.core.Bloom;
import org.ethereum.core.Transaction;
import org.ethereum.util.*;
import org.ethereum.vm.LogInfo;
import org.spongycastle.util.encoders.Hex;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;
import static org.ethereum.util.ByteUtil.EMPTY_BYTE_ARRAY;

/**
 * The transaction receipt is a tuple of three items
 * comprising the transaction, together with the post-transaction state,
 * and the cumulative gas used in the block containing the transaction receipt
 * as of immediately after the transaction has happened,
 */

public class QueryTransactionReceipt {

    private Transaction transaction;
    private byte[] postTxState = EMPTY_BYTE_ARRAY;
    private BigInteger cumulativeGas;
    private Bloom bloomFilter = new Bloom();
    private List<LogInfo> logInfoList = new ArrayList<>();

    private byte[] transactionHash;
    private byte[] contractAddress;
    private BigInteger gasUsed;
    
    private BigInteger transactionIndex;
    private BigInteger height;
    private byte[] blockHash = EMPTY_BYTE_ARRAY;
    private byte[] from;
    private byte[] to;
    private BigInteger timestamp;
   
    private byte[] executionResult = EMPTY_BYTE_ARRAY;
    private String error = "";
    /* Tx Receipt in encoded form */
    private byte[] rlpEncoded;
    
    public QueryTransactionReceipt() {
    }

    public QueryTransactionReceipt(byte[] rlp) {
        RLPList params = RLP.decode2(rlp);
        RLPList receipt = (RLPList) params.get(0);

        RLPItem postTxStateRLP = (RLPItem) receipt.get(0);
        RLPItem cumulativeGasRLP = (RLPItem) receipt.get(1);
        RLPItem bloomRLP = (RLPItem) receipt.get(2);
        RLPItem transactionHashRLP = (RLPItem) receipt.get(3);
        RLPList logs = (RLPList) receipt.get(5);
        RLPItem gasUsedRLP = (RLPItem) receipt.get(6);
        RLPItem contractAddressRLP = (RLPItem) receipt.get(4);

        postTxState = nullToEmpty(postTxStateRLP.getRLPData());
        
        if(cumulativeGasRLP.getRLPData() == null) {
        	cumulativeGas = BigInteger.valueOf(0);
    	}else {
    		cumulativeGas = Numeric.toBigInt(cumulativeGasRLP.getRLPData());
    	}
        
        bloomFilter = new Bloom(bloomRLP.getRLPData());

        if(gasUsedRLP.getRLPData() == null) {
        	gasUsed = BigInteger.valueOf(0);
    	}else {
    		gasUsed = Numeric.toBigInt(gasUsedRLP.getRLPData());
    	}
        
        transactionHash = transactionHashRLP.getRLPData();
        contractAddress =  contractAddressRLP.getRLPData();
        for (RLPElement log : logs) {
            LogInfo logInfo = new LogInfo(log.getRLPData());
            logInfoList.add(logInfo);
        }

        rlpEncoded = rlp;
    }


    public QueryTransactionReceipt(byte[] postTxState, byte[] cumulativeGas,
                              Bloom bloomFilter, List<LogInfo> logInfoList) {
        this.postTxState = postTxState;
        this.cumulativeGas = Numeric.toBigInt(cumulativeGas);
        this.bloomFilter = bloomFilter;
        this.logInfoList = logInfoList;
    }

    public QueryTransactionReceipt(final RLPList rlpList) {
        if (rlpList == null || rlpList.size() != 4)
            throw new RuntimeException("Should provide RLPList with postTxState, cumulativeGas, bloomFilter, logInfoList");

        this.postTxState = rlpList.get(0).getRLPData();
        this.cumulativeGas = Numeric.toBigInt(rlpList.get(1).getRLPData());
        this.bloomFilter = new Bloom(rlpList.get(2).getRLPData());

        List<LogInfo> logInfos = new ArrayList<>();
        for (RLPElement logInfoEl: (RLPList) rlpList.get(3)) {
            LogInfo logInfo = new LogInfo(logInfoEl.getRLPData());
            logInfos.add(logInfo);
        }
        this.logInfoList = logInfos;
    }

    public byte[] getPostTxState() {
        return postTxState;
    }

    public BigInteger getCumulativeGas() {
        return cumulativeGas;
    }

    public BigInteger getGasUsed() {
        return gasUsed;
    }

    public byte[] getExecutionResult() {
        return executionResult;
    }

    public BigInteger getCumulativeGasLong() {
        return cumulativeGas;
    }
    
    public byte[] getTransactionHash() {
    	return transactionHash;
    }
    
    public byte[] getContractAddress() {
    	return contractAddress;
    }

    public Bloom getBloomFilter() {
        return bloomFilter;
    }

    public List<LogInfo> getLogInfoList() {
        return logInfoList;
    }

    public boolean isValid() {
        return gasUsed.floatValue() > 0;
    }

    public boolean isSuccessful() {
        return error.isEmpty();
    }

    public String getError() {
        return error;
    }

    /**
     *  Used for Receipt trie hash calculation. Should contain only the following items encoded:
     *  [postTxState, cumulativeGas, bloomFilter, logInfoList]
     */
    public byte[] getReceiptTrieEncoded() {
        return getEncoded(true);
    }

    /**
     * Used for serialization, contains all the receipt data encoded
     */
    public byte[] getEncoded() {
        if (rlpEncoded == null) {
            rlpEncoded = getEncoded(false);
        }

        return rlpEncoded;
    }

    public byte[] getEncoded(boolean receiptTrie) {

        byte[] postTxStateRLP = RLP.encodeElement(this.postTxState);
        byte[] cumulativeGasRLP = RLP.encodeElement(this.cumulativeGas.toByteArray());
        byte[] bloomRLP = RLP.encodeElement(this.bloomFilter.getData());

        final byte[] logInfoListRLP;
        if (logInfoList != null) {
            byte[][] logInfoListE = new byte[logInfoList.size()][];

            int i = 0;
            for (LogInfo logInfo : logInfoList) {
                logInfoListE[i] = logInfo.getEncoded();
                ++i;
            }
            logInfoListRLP = RLP.encodeList(logInfoListE);
        } else {
            logInfoListRLP = RLP.encodeList();
        }

        return receiptTrie ?
                RLP.encodeList(postTxStateRLP, cumulativeGasRLP, bloomRLP, logInfoListRLP):
                RLP.encodeList(postTxStateRLP, cumulativeGasRLP, bloomRLP, logInfoListRLP,
                        gasUsed.toByteArray(), RLP.encodeElement(executionResult),
                        RLP.encodeElement(error.getBytes(StandardCharsets.UTF_8)));

    }

    public void setPostTxState(byte[] postTxState) {
        this.postTxState = postTxState;
        rlpEncoded = null;
    }

    public void setCumulativeGas(BigInteger cumulativeGas) {
        this.cumulativeGas = cumulativeGas;
        rlpEncoded = null;
    }

    public void setGasUsed(BigInteger gasUsed) {
        this.gasUsed = gasUsed;
        rlpEncoded = null;
    }

    public void setExecutionResult(byte[] executionResult) {
        this.executionResult = executionResult;
        rlpEncoded = null;
    }

    public void setError(String error) {
        this.error = error == null ? "" : error;
    }

    public void setLogInfoList(List<LogInfo> logInfoList) {
        if (logInfoList == null) return;
        this.logInfoList = logInfoList;

        for (LogInfo loginfo : logInfoList) {
            bloomFilter.or(loginfo.getBloom());
        }
        rlpEncoded = null;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public void setContractAddress(byte[] contractAddress) {
    	this.contractAddress = contractAddress;
    }
    
    public void setTransactionHash(byte[] transactionHash) {
    	this.transactionHash = transactionHash;
    }

    public Transaction getTransaction() {
        if (transaction == null) throw new NullPointerException("Transaction is not initialized. Use TransactionInfo and BlockStore to setup Transaction instance");
        return transaction;
    }
    
    public byte[] getBlockHash() {
        return blockHash;
    }

    public BigInteger getHeight() {
        return height;
    }

    public BigInteger getTransactionIndex() {
        return transactionIndex;
    }
    
    
    public BigInteger getTimestamp() {
        return timestamp;
    }
    
    public byte[] getFrom() {
    	return from;
    }
    
    public byte[] getTo() {
    	return to;
    }
    
    public void setBlockHash(byte[] blockHash) {
        this.blockHash = blockHash;
    }
    
    public void setBlockHeight(byte[] height) {
        this.height = Numeric.toBigInt(height);
    }
    
    public void setTransactionIndex(BigInteger transactionIndex) {
        this.transactionIndex = transactionIndex;
    }
    
    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setFrom(byte[] from) {
    	this.from = from;
    }
    
    public void setTo(byte[] to) {
    	this.to = to;
    }

    public ReceiptObject getReceiptObject(){
        ReceiptObject object = new ReceiptObject();
        object.setBloom(bloomFilter.toString());
        object.setContractAddress(Hex.toHexString(to));
        object.setTxHash(Hex.toHexString(transactionHash));
        object.setCumulativeGasUsed(cumulativeGas);
        object.setGasUsed(gasUsed);
        object.setHeight(height);
        object.setPostState(Hex.toHexString(postTxState));
        ReceiptLogs[] logs = new ReceiptLogs[logInfoList.size()];
        for(int i=0;i<logInfoList.size();i++){
            LogInfo logInfo = logInfoList.get(i);
            ReceiptLogs inner = new ReceiptLogs();
            inner.setAddress(Hex.toHexString(logInfo.getAddress()));
            inner.setData(Hex.toHexString(logInfo.getData()));
            String[] topics = new String[logInfo.getTopics().size()];
            for (int j=0;j<logInfo.getTopics().size();j++) {
                String topicStr = Hex.toHexString(logInfo.getTopics().get(j).getData());
                topics[j] = topicStr;
            }
            inner.setTopics(topics);
            logs[i] = inner;
        }

        return object;
    }

    @Override
    public String toString() {
    	return "{\"postTxState\":\"" + Hex.toHexString(postTxState)+"\"" +
                "\n  , \"cumulativeGas\":" + cumulativeGas +
                "\n  , \"Bloom\":\"" + bloomFilter.toString() +
                "\n  , \"Logs\":[{" + logInfoList +
                "\n  , \"transactionHash\":" + Hex.toHexString(transactionHash) +
                "\n  , \"contractAddress\":" + Hex.toHexString(contractAddress) +
                "\n  , \"gasUsed\":" + gasUsed +
                "\n  , \"transactionIndex\":" +transactionIndex +
                "\n  , \"height\":" + height +
                "\n  , \"blockhash\":" +Hex.toHexString(blockHash)+
                "\n  , \"from\":" +Hex.toHexString(from)+
                "\n  , \"to\":" + Hex.toHexString(to)+
                "\n  , \"time\":" + timestamp +
                '}';
    }
}
