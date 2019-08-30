package com.genesis.api.crypto;

import com.genesis.api.bean.exception.LIBException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;


public class PrivateKeyECDSA implements PrivateKey {

    public final static String CryptoTypeName = "ECDSA";

    private Credentials credentials;
    private PublicKey publicKey;

    public static PrivateKey Create(){
        ECKeyPair ecKeyPair = null;
        try {
            ecKeyPair = Keys.createEcKeyPair();
        } catch (Exception e) {
            throw new LIBException(e);
        }
        byte[] privKeyBytes = Numeric.toBytesPadded(ecKeyPair.getPrivateKey(), 32);
        return new PrivateKeyECDSA(Numeric.toHexString(privKeyBytes));
    }

    public PrivateKeyECDSA(String privKey){
        credentials = Credentials.create(privKey);
        publicKey = new PublicKeyECDSA(credentials.getEcKeyPair().getPublicKey());
    }

    @Override
    public byte[] toBytes() {
        BigInteger num = credentials.getEcKeyPair().getPrivateKey();
        return Numeric.toBytesPadded(num, 32);
    }

    @Override
    public Signature sign(byte[] msg) {
      Sign.SignatureData sig = Sign.signMessage(msg, credentials.getEcKeyPair());
      return new SignatureECDSA(sig);
    }

    @Override
    public PublicKey getPubKey() {
        return this.publicKey;
    }

    @Override
    public String toHexString() {
        byte[] privBytes = Numeric.toBytesPadded(credentials.getEcKeyPair().getPrivateKey(), 32);
        return Numeric.toHexString(privBytes);
    }

    @Override
    public String getCryptoType() {
        return this.CryptoTypeName;
    }

    @Override
    public String getAddress() {
        return this.getPubKey().getAddress();
    }
}
