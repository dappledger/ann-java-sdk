package com.rendez.api.crypto.blockdb;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import com.rendez.api.bean.exception.CryptoException;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.util.Arrays;

public class Crypto {

    private static final String PROVIDER = "BC";
    private static final String ECDSA = "ECDSA";
    private static final String SHA256_WITH_ECDSA = "SHA256withECDSA";
    private static final String CURVE_NAME = "curve25519";
    private static SecureRandom random = new SecureRandom();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static KeyPair generateKeys() throws CryptoException {
        try {
            X9ECParameters ecP = CustomNamedCurves.getByName(CURVE_NAME);
            ECParameterSpec ecSpec = new ECParameterSpec(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(),
                    ecP.getSeed());

            KeyPairGenerator keygen = KeyPairGenerator.getInstance(ECDSA, PROVIDER);
            keygen.initialize(ecSpec, new SecureRandom());
            return keygen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new CryptoException(e);
        }
    }

    public static KeyPair makeKeyPair(byte[] compressedPrivate, byte[] compressedPublic) throws CryptoException {
        return new KeyPair(getPublicKeyFromBytes(compressedPublic), getPrivateKeyFromBytes(compressedPrivate));
    }

    public static ECPrivateKey parseCompressedPrivateKey(byte[] compressedPrivateKey) throws CryptoException {

        X9ECParameters ecP = CustomNamedCurves.getByName(CURVE_NAME);
        ECParameterSpec ecSpec = new ECParameterSpec(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(), ecP.getSeed());

        ECPrivateKeySpec privkeyspec = new ECPrivateKeySpec(new BigInteger(compressedPrivateKey), ecSpec);

        PrivateKey generatePrivate = null;
        try {
            generatePrivate = KeyFactory.getInstance(ECDSA, PROVIDER).generatePrivate(privkeyspec);
        } catch (Exception e) {
            throw new CryptoException(e);
        }

        return (ECPrivateKey) generatePrivate;
    }

    public static byte[] compressedKey(PublicKey key) throws CryptoException {
        if (key instanceof ECPublicKey) {
            byte[] encoded = ((ECPublicKey) key).getQ().getEncoded(true);
            return Arrays.copyOfRange(encoded, 1, 33);
        }
        throw new CryptoException(new IllegalArgumentException("Can only compress ECPublicKey"));
    }

    public static byte[] compressedKey(PrivateKey privkey) throws CryptoException {
        if (privkey instanceof ECPrivateKey) {
            return ((ECPrivateKey) privkey).getD().toByteArray();
        }
        throw new CryptoException(new IllegalArgumentException("Can only compress ECPrivateKey"));
    }

    public static byte[] signMessage(PrivateKey key, byte[] message) {

        try {
            Signature ecdsaSign = Signature.getInstance(SHA256_WITH_ECDSA, PROVIDER);
            ecdsaSign.initSign(key);
            ecdsaSign.update(message);
            byte[] signature = ecdsaSign.sign();
            return signature;
        } catch (NoSuchAlgorithmException | NoSuchProviderException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ECPublicKeySpec getKeySpec(byte[] pubOrPrivKey) {
        X9ECParameters ecP = CustomNamedCurves.getByName(CURVE_NAME);
        ECParameterSpec ecSpec = new ECParameterSpec(ecP.getCurve(), ecP.getG(), ecP.getN(), ecP.getH(), ecP.getSeed());
        ECNamedCurveSpec params = new ECNamedCurveSpec(CURVE_NAME, ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN());
        ECPoint point = ECPointUtil.decodePoint(params.getCurve(), pubOrPrivKey);
        ECPublicKeySpec keySpec = new ECPublicKeySpec(point, params);
        return keySpec;
    }

    public static PrivateKey getPrivateKeyFromBytes(byte[] privKey) throws CryptoException {
        if (privKey.length > 0 && privKey.length < 33) {
            return parseCompressedPrivateKey(privKey);
        } else {
            try {
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privKey);
                KeyFactory factory = KeyFactory.getInstance(ECDSA, PROVIDER);
                PrivateKey privateKey = factory.generatePrivate(spec);
                return privateKey;
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        }
    }

    public static PublicKey getPublicKeyFromBytes(byte[] pubKey) throws CryptoException {
        try {
            KeyFactory kf = KeyFactory.getInstance(ECDSA, PROVIDER);
            return kf.generatePublic(getKeySpec(pubKey));
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    public static boolean verifySignature(byte[] compressedPublicKey, byte[] message, byte[] signature) {
        try {
            PublicKey publicKey = getPublicKeyFromBytes(compressedPublicKey);
            return verifySignature(publicKey, message, signature);
        } catch (CryptoException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean verifySignature(PublicKey pubkey, byte[] message, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance(SHA256_WITH_ECDSA, PROVIDER);
            ecdsaVerify.initVerify(pubkey);
            ecdsaVerify.update(message);
            boolean result = ecdsaVerify.verify(signature);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String randomString(int length) {
        return new BigInteger(length * 5, random).toString(32);
    }

    public static byte[] ripemd160(byte[] bytesToHash) throws CryptoException {
        try {
            MessageDigest md = MessageDigest.getInstance("RIPEMD160");
            return md.digest(bytesToHash);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static String toString00(byte[] bArr) {
        if (bArr == null)
            return null;

        final StringBuilder builder = new StringBuilder();
        for (byte b : bArr) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString().toUpperCase();
    }
}
