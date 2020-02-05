package com.genesis.api.bean.model;

import static org.ethereum.util.ByteUtil.EMPTY_BYTE_ARRAY;
import java.math.BigInteger;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPItem;
import org.ethereum.util.RLPList;
import org.web3j.utils.Numeric;

public class RawTransactionData {
	private BigInteger nonce;
    private byte[] to = EMPTY_BYTE_ARRAY;
    private BigInteger value;
    private BigInteger gas;
    private BigInteger gasprice;
    private byte[] input = EMPTY_BYTE_ARRAY;
    
    private BigInteger r;
    private BigInteger v;
    private BigInteger s;
      
    public RawTransactionData() {
    	
    }
    
    public RawTransactionData(byte[] rlp) {
    	RLPList params = RLP.decode2(rlp);
        RLPList result = (RLPList) params.get(0);
        
        RLPItem accountNonceRLP = (RLPItem) result.get(0);
        RLPItem priceRLP = (RLPItem) result.get(1);
        RLPItem gasLimitRLP = (RLPItem) result.get(2);
        RLPItem recipientRLP = (RLPItem) result.get(3);
        RLPItem amountRLP = (RLPItem) result.get(4);
        RLPItem inputRLP = (RLPItem) result.get(5);
        
        RLPItem vRLP = (RLPItem) result.get(6);
        RLPItem rRLP = (RLPItem) result.get(7);
        RLPItem sRLP = (RLPItem) result.get(8);
      
        if(accountNonceRLP.getRLPData() == null) {
        	nonce = BigInteger.valueOf(0);
    	}else {
    		nonce = Numeric.toBigInt(accountNonceRLP.getRLPData());
    	}
        
        if(recipientRLP.getRLPData() == null) {
        	to = EMPTY_BYTE_ARRAY;
    	}else {
    		to = recipientRLP.getRLPData();
    	}
        
        if(gasLimitRLP.getRLPData() == null) {
        	gas = BigInteger.valueOf(0);
    	}else {
    		gas = Numeric.toBigInt(gasLimitRLP.getRLPData());
    	}
        
        if(amountRLP.getRLPData() == null) {
        	value = BigInteger.valueOf(0);
    	}else {
    		value = Numeric.toBigInt(amountRLP.getRLPData());
    	}
        
        if(priceRLP.getRLPData() == null) {
        	gasprice = BigInteger.valueOf(0);
    	}else {
    		gasprice = Numeric.toBigInt(priceRLP.getRLPData());
    	}
        
        input = inputRLP.getRLPData();
        v = Numeric.toBigInt(vRLP.getRLPData());
        r = Numeric.toBigInt(rRLP.getRLPData());
        s = Numeric.toBigInt(sRLP.getRLPData());
    } 
    
    public void setNonce(BigInteger nonce) {
    	this.nonce = nonce;
    }
    
    public BigInteger getNonce() {
    	return nonce;
    }
    
    public void setTo(byte[] recipient) {
    	this.to = recipient;
    }
    
    public byte[] getTo() {
    	return to;
    }
    
    public void setValue(BigInteger amount) {
    	this.value = amount;
    }
    
    public BigInteger getValue() {
    	return value;
    }
    
    public void setGas(BigInteger gasLimit) {
    	this.gas = gasLimit;
    }
    
    public BigInteger getGas() {
    	return gas;
    }
    
    public void setGasprice(byte[] price) {
    	if(price == null) {
    		this.gasprice = BigInteger.valueOf(0);
    	}
    	this.gasprice = Numeric.toBigInt(price);
    }
    
    public BigInteger getGasprice() {
    	return gasprice;
    }
    
    public void setInput(byte[] data) {
    	this.input = data;
    }
    
    public byte[] getInput() {
    	return input;
    }
    
    public void setV(BigInteger v) {
    	this.v = v;
    }
    
    public BigInteger getV() {
    	return v;
    }
    
    public void setR(BigInteger r) {
    	this.r = r;
    }
    
    public BigInteger getR() {
    	return r;
    }
    
    public void setS(BigInteger s) {
    	this.s = s;
    }
    
    public BigInteger getS() {
    	return s;
    }
}

