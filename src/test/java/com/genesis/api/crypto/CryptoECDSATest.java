package com.genesis.api.crypto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.utils.Numeric;


@Slf4j
public class CryptoECDSATest {
    String privKeyStr = "0xae08dc67186f140235a36a06e55dc2ccabbc5365525825c382aa36e055de84cd";
    PrivateKeyECDSA privKey = new PrivateKeyECDSA(privKeyStr);
    byte[] src = Numeric.hexStringToByteArray("0102030405060708010203040506070801020304050607080102030405060708");
    private byte v= 28;
    private byte[] r= Numeric.hexStringToByteArray("0x8465d1bba332b6638228792f43b5b50b29ea0f41c7722afb72fdb943f7da08be");
    private byte[] s= Numeric.hexStringToByteArray("0x4701fff3c25bd78f9f5999a1eb331a1252af1e0dfcd08f1e2b70912200d551c0");
    @Test
    public void testCreate() {

        for (int i=0;i<1000;i++){
            String privKey = PrivateKeyECDSA.Create().toHexString();
            Assert.assertTrue(privKey.startsWith("0x"));
            Assert.assertEquals(66, privKey.length());
        }
    }

    @Test
    public void testToHexString() {
        Assert.assertEquals(privKey.toHexString(), privKeyStr);
    }


    @Test
    public void testVerify(){
        Assert.assertTrue(privKey.getPubKey().verifyBytes(src, new SignatureECDSA(v, r, s)));
    }

    @Test
    public void testSignAndVerify(){
        SignatureECDSA sig = (SignatureECDSA) privKey.sign(src);
        Assert.assertTrue(privKey.getPubKey().verifyBytes(src, sig));
    }
}