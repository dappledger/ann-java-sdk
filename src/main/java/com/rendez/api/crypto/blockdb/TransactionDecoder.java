package com.rendez.api.crypto.blockdb;

import com.rendez.api.blockdb.BlockDbTransaction;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class TransactionDecoder {
    public TransactionDecoder() {
    }

    public static BlockDbTransaction decode(String hexTransaction) {
        byte[] transaction = Numeric.hexStringToByteArray(hexTransaction);
        RlpList rlpList = RlpDecoder.decode(transaction);
        RlpList values = (RlpList)rlpList.getValues().get(0);

        String from = ((RlpString)values.getValues().get(0)).asString();
        BigInteger timestamp = ((RlpString)values.getValues().get(1)).asPositiveBigInteger();
        byte[] value = ((RlpString)values.getValues().get(2)).getBytes();

        if (values.getValues().size() > 3) {
            byte v = ((RlpString)values.getValues().get(3)).getBytes()[0];
            byte[] r = Numeric.toBytesPadded(Numeric.toBigInt(((RlpString)values.getValues().get(4)).getBytes()), 32);
            byte[] s = Numeric.toBytesPadded(Numeric.toBigInt(((RlpString)values.getValues().get(5)).getBytes()), 32);
            Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
            return new SignedBlockDbTransaction(from, timestamp, value, signatureData);
        } else {
            return new BlockDbTransaction(from, timestamp, value,(byte)1);
        }
    }
}
