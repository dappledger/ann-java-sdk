package com.genesis.api.bean.model;

import org.web3j.utils.Numeric;

import lombok.Data;

@Data
public class QueryTransactionPayload {
	private String payload;
    private String errMsg;
    
    public void setPayload(String p) {
    	payload = hexToASCII(p);
    }
    
    public String hexToASCII(String hexValue){
    	byte[] baKeyword = new byte[hexValue.length() / 2];
 	    for (int i = 0; i < baKeyword.length; i++) {
 	    	try {
 	    		baKeyword[i] = (byte) (0xff & Integer.parseInt(hexValue.substring(
 	    				i * 2, i * 2 + 2), 16));
 	        } catch (Exception e) {
 	        	e.printStackTrace();
 	        }
 	    }
 	    try {
 	    	hexValue = new String(baKeyword, "utf-8");// UTF-16le:Not
 	    } catch (Exception e1) {
 	    	e1.printStackTrace();
 	    }
 	    return hexValue;
    }
}

