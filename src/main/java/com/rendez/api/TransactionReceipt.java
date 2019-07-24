package com.rendez.api;

import org.ethereum.core.Bloom;
import org.ethereum.core.Transaction;
import org.ethereum.util.*;
import org.ethereum.vm.LogInfo;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;
import static org.ethereum.util.ByteUtil.EMPTY_BYTE_ARRAY;

/**
 * The transaction receipt is a tuple of three items
 * comprising the transaction, together with the post-transaction state,
 * and the cumulative gas used in the block containing the transaction receipt
 * as of immediately after the transaction has happened,
 */
public class TransactionReceipt {

    private Transaction transaction;



    private String txHash;
    private BigInteger height;
    private BigInteger time;

    public BigInteger getHeight() {
        return height;
    }

    public void setHeight(BigInteger height) {
        this.height = height;
    }

    public BigInteger getTime() {
        return time;
    }

    public void setTime(BigInteger time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    private String from;
    private String to;
    private String contractAddress;

    private byte[] postTxState = EMPTY_BYTE_ARRAY;
    private byte[] cumulativeGas = EMPTY_BYTE_ARRAY;
    private Bloom bloomFilter = new Bloom();
    private List<LogInfo> logInfoList = new ArrayList<>();

    private byte[] gasUsed = EMPTY_BYTE_ARRAY;
    private byte[] executionResult = EMPTY_BYTE_ARRAY;
    private String error = "";




    /* Tx Receipt in encoded form */
    private byte[] rlpEncoded;

    public TransactionReceipt() {
    }

    public TransactionReceipt(byte[] rlp) {
        RLPList params = RLP.decode2(rlp);
        RLPList receipt = (RLPList) params.get(0);

        RLPItem heightRLP = (RLPItem) receipt.get(0);
        height = new BigInteger(1,heightRLP.getRLPData());

        RLPItem timestampRLP = (RLPItem) receipt.get(1);
        time = new BigInteger(1,timestampRLP.getRLPData());

        RLPItem fromRLP = (RLPItem) receipt.get(2);
        from = com.rendez.api.util.ByteUtil.bytesToHex(fromRLP.getRLPData());
        RLPItem toRLP = (RLPItem) receipt.get(3);
        to = com.rendez.api.util.ByteUtil.bytesToHex(toRLP.getRLPData());

        RLPItem postTxStateRLP = (RLPItem) receipt.get(4);

        RLPItem cumulativeGasRLP = (RLPItem) receipt.get(5);

        RLPItem bloomRLP = (RLPItem) receipt.get(6);
        RLPItem txHashRLP = (RLPItem) receipt.get(7);
        txHash = com.rendez.api.util.ByteUtil.bytesToHex(txHashRLP.getRLPData());
        RLPItem contractAddressRLP = (RLPItem) receipt.get(8);
        contractAddress = com.rendez.api.util.ByteUtil.bytesToHex(contractAddressRLP.getRLPData());

        RLPList logs = (RLPList) receipt.get(9);
        RLPItem gasUsedRLP = (RLPItem) receipt.get(10);

        RLPItem statusRLP = (RLPItem) receipt.get(11);

        postTxState = nullToEmpty(postTxStateRLP.getRLPData());
        cumulativeGas = cumulativeGasRLP.getRLPData();
        bloomFilter = new Bloom(bloomRLP.getRLPData());
        gasUsed = gasUsedRLP.getRLPData();
        executionResult = (executionResult = txHashRLP.getRLPData()) == null ? EMPTY_BYTE_ARRAY : executionResult;
        for (RLPElement log : logs) {
            LogInfo logInfo = new LogInfo(log.getRLPData());
            logInfoList.add(logInfo);
        }

        rlpEncoded = rlp;
    }

    //修复一些rlp解码后的byte缺位
    public  byte[] fixByte(byte[] v) {
        if(v.length == 1 && v[0] <0){
            byte[] result = new byte[2];
            result[0] = 0;
            result[1] = v[0];
            return result;
        }
        return v;
    }

//    public TransactionReceipt(byte[] postTxState, byte[] cumulativeGas,
//                              Bloom bloomFilter, List<LogInfo> logInfoList) {
//        this.postTxState = postTxState;
//        this.cumulativeGas = cumulativeGas;
//        this.bloomFilter = bloomFilter;
//        this.logInfoList = logInfoList;
//    }
//
//    public TransactionReceipt(final RLPList rlpList) {
//        if (rlpList == null || rlpList.size() != 4)
//            throw new RuntimeException("Should provide RLPList with postTxState, cumulativeGas, bloomFilter, logInfoList");
//
//        this.postTxState = rlpList.get(0).getRLPData();
//        this.cumulativeGas = rlpList.get(1).getRLPData();
//        this.bloomFilter = new Bloom(rlpList.get(2).getRLPData());
//
//        List<LogInfo> logInfos = new ArrayList<>();
//        for (RLPElement logInfoEl: (RLPList) rlpList.get(3)) {
//            LogInfo logInfo = new LogInfo(logInfoEl.getRLPData());
//            logInfos.add(logInfo);
//        }
//        this.logInfoList = logInfos;
//    }

    public byte[] getPostTxState() {
        return postTxState;
    }

    public byte[] getCumulativeGas() {
        return cumulativeGas;
    }

    public byte[] getGasUsed() {
        return gasUsed;
    }

    public byte[] getExecutionResult() {
        return executionResult;
    }

    public long getCumulativeGasLong() {
        return new BigInteger(1, cumulativeGas).longValue();
    }


    public Bloom getBloomFilter() {
        return bloomFilter;
    }

    public List<LogInfo> getLogInfoList() {
        return logInfoList;
    }

    public boolean isValid() {
        return ByteUtil.byteArrayToLong(gasUsed) > 0;
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
        byte[] cumulativeGasRLP = RLP.encodeElement(this.cumulativeGas);
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
                        RLP.encodeElement(gasUsed), RLP.encodeElement(executionResult),
                        RLP.encodeElement(error.getBytes(StandardCharsets.UTF_8)));

    }

    public void setPostTxState(byte[] postTxState) {
        this.postTxState = postTxState;
        rlpEncoded = null;
    }

    public void setCumulativeGas(long cumulativeGas) {
        this.cumulativeGas = BigIntegers.asUnsignedByteArray(BigInteger.valueOf(cumulativeGas));
        rlpEncoded = null;
    }

    public void setCumulativeGas(byte[] cumulativeGas) {
        this.cumulativeGas = cumulativeGas;
        rlpEncoded = null;
    }

    public void setGasUsed(byte[] gasUsed) {
        this.gasUsed = gasUsed;
        rlpEncoded = null;
    }

    public void setGasUsed(long gasUsed) {
        this.gasUsed = BigIntegers.asUnsignedByteArray(BigInteger.valueOf(gasUsed));
        rlpEncoded = null;
    }

    public void setExecutionResult(byte[] executionResult) {
        this.executionResult = executionResult;
        rlpEncoded = null;
    }
    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
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

    public Transaction getTransaction() {
        if (transaction == null) throw new NullPointerException("Transaction is not initialized. Use TransactionInfo and BlockStore to setup Transaction instance");
        return transaction;
    }



    @Override
    public String toString() {

        // todo: fix that

        return "TransactionReceipt[" +
                "\n  , height=" + height +
                "\n  , time=" + time +
                "\n  , from=" + from +
                "\n  , to=" + to +
                "\n  , contractAddress=" + contractAddress +
                "\n  , postTxState=" + Hex.toHexString(postTxState) +
                "\n  , cumulativeGas=" + Hex.toHexString(cumulativeGas) +
                "\n  , gasUsed=" + Hex.toHexString(gasUsed) +
                "\n  , error=" + error +
                "\n  , executionResult=" + Hex.toHexString(executionResult) +
                "\n  , bloom=" + bloomFilter.toString() +
                "\n  , logs=" + logInfoList +
                ']';
    }

}
