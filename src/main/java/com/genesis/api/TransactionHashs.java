package com.genesis.api;

public class TransactionHashs {
	private String[] hashs;
	private long  count;
	
	TransactionHashs(){
		
	}
	
	TransactionHashs(String[] txhashs, long num){
		hashs = txhashs;
		count = num;
	}
	
	public void setHashs(String[] hashs) {
		this.hashs = hashs;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public String[] getHashs() {
		return hashs;
	}
	
	public long getCount(){
		return count;
	}
}
