package com.genesis.api;

import org.web3j.utils.Numeric;

public enum QueryType {
    Contract("0"),
    Nonce("1"),
    Receipt("3"),
    Payload("5"),
	ContractByHeight("10"),
	Key("11"),
	KeyPrefix("12"),
	PendingNonce("13");

    QueryType(String code) {
        this.code = new Byte(code);
    }

    byte code;

    public byte getCode() {
        return code;
    }

    public String padd(String address) {
        byte[] addressByte = Numeric.hexStringToByteArray(address);
        byte[] resp = new byte[addressByte.length + 1];
        resp[0] = this.getCode();
        System.arraycopy(addressByte, 0, resp, 1, addressByte.length);
        return Numeric.toHexString(resp);
    }
    
    public String ppadd(String address) { 
        byte[] addressByte = Numeric.hexStringToByteArray(address);
        byte[] resp = new byte[addressByte.length + 2];
        resp[0] = this.getCode();
        resp[1] = 0x01;
        System.arraycopy(addressByte, 0, resp, 2, addressByte.length);
        return Numeric.toHexString(resp);

    }

    public String paddByte(byte[] txBytes) {
        return padd(Numeric.toHexString(txBytes));
    }
    
    public String paddString(String str) {
        byte[] strByte = str.getBytes();
        byte[] resp = new byte[strByte.length + 1];
        resp[0] = this.getCode();
        System.arraycopy(strByte, 0, resp, 1, strByte.length);
        return Numeric.toHexString(resp);
    }
}
