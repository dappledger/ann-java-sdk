package com.genesis.api.bean.model;

import org.ethereum.util.RLP;
import org.ethereum.util.RLPItem;
import org.ethereum.util.RLPList;
import org.spongycastle.util.encoders.Hex;

public class TransactionKVTx {
	private byte[] key;
	private byte[] value;

	public TransactionKVTx() {
	}
	
	public TransactionKVTx(byte[] rlp) {
        RLPList params = RLP.decode2(rlp);
        System.out.print(params.size());

        RLPItem keyRLP = (RLPItem) params.get(0);
        RLPItem valueRLP = (RLPItem) params.get(1);
        
        key = keyRLP.getRLPData();
        value =  valueRLP.getRLPData();
	}

	public TransactionKVTx(byte[] k,byte[] v) {
		key = k;
		value = v;
	}
	
	public String getKey() {
		return hexToASCII(Hex.toHexString(key));
	} 
	
	public String getValue() {
		return hexToASCII(Hex.toHexString(value));
	}
	
	public void setKey(byte[] k) {
		key = k;
	}
	
	public void setValue(byte[] v) {
		value = v;
	}
	
	public String hexToASCII(String hexValue)
	   {
	       StringBuilder output = new StringBuilder("");
	       for (int i = 0; i < hexValue.length(); i += 2)
	       {
	           String str = hexValue.substring(i, i + 2);
	           output.append((char) Integer.parseInt(str, 16));
	       }
	       return output.toString();
	   }
}
