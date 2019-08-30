package com.genesis.api;

import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;
import static org.ethereum.util.ByteUtil.EMPTY_BYTE_ARRAY;

import java.math.BigInteger;

import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import lombok.extern.slf4j.Slf4j;

import org.ethereum.core.Bloom;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPElement;
import org.ethereum.util.RLPItem;
import org.ethereum.util.RLPList;
import org.ethereum.vm.LogInfo;
import org.spongycastle.util.encoders.Hex;

@Slf4j
public class ResultTransaction {
//	private byte[] blockHash = EMPTY_BYTE_ARRAY;
//    private BigInteger blockHeight;
//    private BigInteger transactionIndex;
//    private byte[] rawTransaction = EMPTY_BYTE_ARRAY;
//    private BigInteger timestamp;
	
	private byte[] blockHash = EMPTY_BYTE_ARRAY;
    private byte[] blockHeight;
    private BigInteger transactionIndex;
    private byte[] rawTransaction = EMPTY_BYTE_ARRAY;
    private BigInteger timestamp;
    
    public ResultTransaction() {
    	
    }
    
    public ResultTransaction(byte[] rlp) {
    	RLPList params = RLP.decode2(rlp);
        RLPList result = (RLPList) params.get(0);
        
        RLPItem blockHashRLP = (RLPItem) result.get(0);
        RLPItem blockHeightRLP = (RLPItem) result.get(1);
        RLPItem transactionIndexRLP = (RLPItem) result.get(2);
        RLPItem rawTransactionRLP = (RLPItem) result.get(3);
        RLPItem timestampRLP = (RLPItem) result.get(4);
      
        blockHash = blockHashRLP.getRLPData();
        blockHeight = blockHeightRLP.getRLPData();
        rawTransaction = rawTransactionRLP.getRLPData();
        timestamp = Numeric.toBigInt(timestampRLP.getRLPData());
        
        if(transactionIndexRLP.getRLPData() == null) {
        	transactionIndex = BigInteger.valueOf(0);
    	}else {
    		transactionIndex = Numeric.toBigInt(transactionIndexRLP.getRLPData());
    	}
    }    
    
    public byte[] getBlockHash() {
        return blockHash;
    }

    public byte[] getBlockHeight() {
        return blockHeight;
    }

    public BigInteger getTransactionIndex() {
        return transactionIndex;
    }
    
    public byte[] getRawTransaction() {
        return rawTransaction;
    }
    
    public BigInteger getTimestamp() {
        return timestamp;
    }
    
    public void setBlockHash(byte[] blockHash) {
        this.blockHash = blockHash;
    }
    
    public void setBlockHeight(byte[] blockHeight) {
        this.blockHeight = blockHeight;
    }
    
    public void setTransactionIndex(BigInteger transactionIndex) {
        this.transactionIndex = transactionIndex;
    }
    
    public void setRawTransaction(byte[] rawTransaction) {
        this.rawTransaction = rawTransaction;
    }
    
    public void setTimestamp(BigInteger timestamp) {
        this.timestamp = timestamp;
    }
}
