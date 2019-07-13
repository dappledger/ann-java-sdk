//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.rendez.api.crypto.blockdb;

import java.math.BigInteger;
import java.security.SignatureException;

import com.rendez.api.TransactionUtil;
import com.rendez.api.blockdb.BlockDbTransaction;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.crypto.TransactionEncoder;

public class SignedBlockDbTransaction extends BlockDbTransaction {
    private static final int CHAIN_ID_INC = 35;
    private static final int LOWER_REAL_V = 27;
    private SignatureData signatureData;

    public SignedBlockDbTransaction(String address,BigInteger timestamp,byte[] value, SignatureData signatureData) {
        super(address,timestamp,value,(byte)1);
        this.signatureData = signatureData;
    }

    public String getFrom() throws SignatureException {
        Integer chainId = this.getChainId();
        byte[] encodedTransaction;
        if (null == chainId) {
            encodedTransaction = TransactionUtil.encode(this);
        } else {
            encodedTransaction = TransactionUtil.encode(this, chainId.byteValue());
        }

        byte v = this.signatureData.getV();
        byte[] r = this.signatureData.getR();
        byte[] s = this.signatureData.getS();
        SignatureData signatureDataV = new SignatureData(this.getRealV(v), r, s);
        BigInteger key = Sign.signedMessageToKey(encodedTransaction, signatureDataV);
        return "0x" + Keys.getAddress(key);
    }

    public void verify(String from) throws SignatureException {
        String actualFrom = this.getFrom();
        if (!actualFrom.equals(from)) {
            throw new SignatureException("from mismatch");
        }
    }

    public SignatureData getSignatureData() {
        return this.signatureData;
    }

    private byte getRealV(byte v) {
        if (v != 27 && v != 28) {
            byte realV = 27;
            int inc = 0;
            if (v % 2 == 0) {
                inc = 1;
            }

            return (byte)(realV + inc);
        } else {
            return v;
        }
    }

    public Integer getChainId() {
        byte v = this.signatureData.getV();
        if (v != 27 && v != 28) {
            Integer chainId = (v - 35) / 2;
            return chainId;
        } else {
            return null;
        }
    }
}
