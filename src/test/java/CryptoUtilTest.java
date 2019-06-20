import com.rendez.api.CryptoUtil;
import com.rendez.api.TransactionUtil;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.PrivateKeyECDSA;
import com.rendez.api.crypto.Signature;
import com.rendez.api.crypto.SignatureECDSA;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;


@Slf4j
public class CryptoUtilTest {

//    @Test
//    public void TestDecodeTxMsg(){
//        String txmsg = "0xf902eb01808094b6afcb28900292b497c61a773b6110c4addc65e089010000000000000000b90283608060405234801561001057600080fd5b50610263806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632b823efe14610046575b600080fd5b34801561005257600080fd5b5061005b610144565b604051808773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001965050505050505060405180910390f35b600080600080600080600080600080600080735e72914535f202659083db3a02c984188fa26e9f63c6b6e6396040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040160606040518083038186803b1580156101b657600080fd5b505af41580156101ca573d6000803e3d6000fd5b505050506040513d60608110156101e057600080fd5b810190808051906020019092919080519060200190929190805190602001909291905050509550955095503033349250925092508583868487858494509b509b509b509b509b509b505050505050509091929394955600a165627a7a72305820c9ec566747f7c81af44d23293bc4feaaa84b1f02eb61d603c5786d7f95c8584d00291ca0f4cf29659ae8eedb66630e09f62b902f5ee9b87c810397f16c1b032c759f477ca071e763b986b17c085917f6e0e8e10104d908fd3532bd2dfec32287c83cf27ddf";
//        SignedRawTransaction rtx = TransactionUtil.decodeTxMsg(txmsg);
//        Assert.assertEquals(rtx.getNonce(), BigInteger.valueOf(1));
//        Assert.assertEquals(rtx.getTo(), "0xb6afcb28900292b497c61a773b6110c4addc65e0");
//        Assert.assertEquals(rtx.getData(), "608060405234801561001057600080fd5b50610263806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632b823efe14610046575b600080fd5b34801561005257600080fd5b5061005b610144565b604051808773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001965050505050505060405180910390f35b600080600080600080600080600080600080735e72914535f202659083db3a02c984188fa26e9f63c6b6e6396040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040160606040518083038186803b1580156101b657600080fd5b505af41580156101ca573d6000803e3d6000fd5b505050506040513d60608110156101e057600080fd5b810190808051906020019092919080519060200190929190805190602001909291905050509550955095503033349250925092508583868487858494509b509b509b509b509b509b505050505050509091929394955600a165627a7a72305820c9ec566747f7c81af44d23293bc4feaaa84b1f02eb61d603c5786d7f95c8584d0029");
//    }
//
//
//    @Test
//    public void TestEncodeTx(){
//        PrivateKey privateKey = new PrivateKeyECDSA("16183ac81b7888e9c80aa7c586ed9337478fc6d8bf1f11759a9bb0aed41fc987");
//        BigInteger nonce = BigInteger.valueOf(99);
//        List<Type> inputParameters = new ArrayList<>();
//        inputParameters.add(new Utf8String("2"));
//        inputParameters.add(new Utf8String("抽奖"));
//        inputParameters.add(new Utf8String("201901031836"));
//        inputParameters.add(new Utf8String("iphoneXR"));
//        inputParameters.add(new Uint256(10));
//        inputParameters.add(new Address("0x2b35e1555e3d9e31b0099733cda6a6dfa88bbfdd"));
//        List<TypeReference<?>> outputParameters = new ArrayList<>();
//        outputParameters.add(new TypeReference<Uint256>() {});
//        Function function = new Function("initLottery", inputParameters, outputParameters);
//        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, "0xe52c09567254855381395b99decbc84b96de513e", function);
//        byte[] message = TransactionUtil.encode(tx, privateKey);
//        String rawTxmsg = Numeric.toHexString(message);
//        log.info(rawTxmsg);
//    }
//
//    @Test
//    public void TestSignature() throws SignatureException {
//        String data = "608060405234801561001057600080fd5b50610263806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632b823efe14610046575b600080fd5b34801561005257600080fd5b5061005b610144565b604051808773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001965050505050505060405180910390f35b600080600080600080600080600080600080735e72914535f202659083db3a02c984188fa26e9f63c6b6e6396040518163ffffffff167c010000000000000000000000000000000000000000000000000000000002815260040160606040518083038186803b1580156101b657600080fd5b505af41580156101ca573d6000803e3d6000fd5b505050506040513d60608110156101e057600080fd5b810190808051906020019092919080519060200190929190805190602001909291905050509550955095503033349250925092508583868487858494509b509b509b509b509b509b505050505050509091929394955600a165627a7a72305820c9ec566747f7c81af44d23293bc4feaaa84b1f02eb61d603c5786d7f95c8584d0029";
//        String toAddress = "0xb6afcb28900292b497c61a773b6110c4addc65e0";
//        byte v = 28;
//        byte[] r = Numeric.hexStringToByteArray("f4cf29659ae8eedb66630e09f62b902f5ee9b87c810397f16c1b032c759f477c");
//        byte[] s =  Numeric.hexStringToByteArray("71e763b986b17c085917f6e0e8e10104d908fd3532bd2dfec32287c83cf27ddf");
//        BigInteger value = Numeric.toBigInt("0x010000000000000000");
//        RawTransaction rtx = RawTransaction.createTransaction(BigInteger.valueOf(1), BigInteger.valueOf(0), BigInteger.valueOf(0), toAddress, value, data);
//        Signature sig = new SignatureECDSA(v, r, s);
//        String txMsg = Numeric.toHexString(TransactionUtil.encodeWithSig(rtx, sig));
//        SignedRawTransaction signedRawTransaction = (SignedRawTransaction) TransactionDecoder.decode(txMsg);
//        String signerAddress = signedRawTransaction.getFrom();
//        log.info("{}", signerAddress);
//        Assert.assertEquals(signerAddress, "0x0b1f66c31e7ee5674003a304c4c40e078f07c469");
//    }
//
//
//    @Test
//    public void TestHashTx(){
//        String msg = "f8862b8085174876e80094e577c05de9be1818a322b7e61227c7a4d54c543480a4179d375c00000000000000000000000000000000000000000000000000000000000000021ca0936f5aeffe0d60811812c1bd6152707a0e3f0aa093a198fb6cade2ea4f41b9daa0421a414a6981f19e6333710d38ec81dfc05fc9d895257ec60b78c17856be8a13";
//        byte[] txBytes = Numeric.hexStringToByteArray(msg);
//        String txHash = CryptoUtil.txHash(txBytes);
//        log.info("txHash {}", txHash);
//    }
}
