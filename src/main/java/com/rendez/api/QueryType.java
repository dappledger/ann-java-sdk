package com.rendez.api;

import org.web3j.utils.Numeric;

public enum QueryType {
    Contract("0"),
    Nonce("1"),
    Receipt("3"),
    BlockHashs("10");

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

    public String paddByte(byte[] txBytes) {
        return padd(Numeric.toHexString(txBytes));
    }
}
