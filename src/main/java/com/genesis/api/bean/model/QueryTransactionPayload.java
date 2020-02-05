package com.genesis.api.bean.model;

import lombok.Data;

@Data
public class QueryTransactionPayload {
	private String payload;
    private String errMsg;
    
    public void setPayload(String p) {
    	payload = hexToASCII(p);
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

