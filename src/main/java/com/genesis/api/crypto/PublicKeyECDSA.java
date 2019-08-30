package com.genesis.api.crypto;

import com.genesis.api.bean.exception.LIBException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;

public class PublicKeyECDSA implements PublicKey {
    private BigInteger pubkey;

    PublicKeyECDSA(BigInteger pubkeyInt){
        pubkey = pubkeyInt;
    }
    @Override
    public String getAddress() {
        return Keys.getAddress(pubkey);
    }

    @Override
    public byte[] toBytes() {
        return Numeric.toBytesPadded(this.pubkey, 64);
    }

    @Override
    public String toHexString() {
        return Numeric.toHexString(this.toBytes());
    }

    @Override
    public boolean verifyBytes(byte[] msg, Signature sig){
        SignatureECDSA ecdsaSig = (SignatureECDSA) sig;
        try {
            return this.pubkey.equals(Sign.signedMessageToKey(msg, ecdsaSig.getRawSig()));
        } catch (SignatureException e) {
           throw new LIBException(e);
        }
    }
}
