package com.genesis.api.wallet;

import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

import java.io.File;


@Slf4j
public class Wallet {

    private Credentials credentials;

    /**
     *
     * @param password 密码
     * @param path 证书文件路径
     */
    public Wallet(String password, String path) {
        try {
            credentials = WalletUtils.loadCredentials(password, path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param password 密码
     * @param privateKey pk
     * @param path 托管路径
     * @return 文件名
     */
    public static String createWallet(String password, String privateKey, String path) {
        try {
            return WalletUtils.generateWalletFile(password, ECKeyPair.create(privateKey.getBytes()), new File(path), true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
