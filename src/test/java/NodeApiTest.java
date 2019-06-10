
import com.rendez.api.CryptoUtil;
import com.rendez.api.NodeSrv;
import com.rendez.api.TransactionReceipt;
import com.rendez.api.TransactionUtil;
import com.rendez.api.bean.model.DeployContractResult;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.PrivateKeyECDSA;
import com.rendez.api.crypto.Signature;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.ethereum.vm.LogInfo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.crypto.RawTransaction;
import org.web3j.rlp.*;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class NodeApiTest {

    //智能合约二进制代码
    private static final String bCode = "608060405234801561001057600080fd5b506101c3806100206000396000f30060806040526004361061004b5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416632c560ec08114610050578063d0e30db0146100da575b600080fd5b34801561005c57600080fd5b506100656100f1565b6040805160208082528351818301528351919283929083019185019080838360005b8381101561009f578181015183820152602001610087565b50505050905090810190601f1680156100cc5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156100e657600080fd5b506100ef610128565b005b60408051808201909152600481527f6162636400000000000000000000000000000000000000000000000000000000602082015290565b6040805133808252602082015260608183018190526003908201527f6162630000000000000000000000000000000000000000000000000000000000608082015290517f4db2efd59f3e5a97173b6b255ce6ebaea50d7f27654cf39705afa269e2529b3a9181900360a00190a15600a165627a7a7230582042b1e64c23be2b8da97983a39c7730a4a4897b165984a0352f6275d3008f13b90029";

    //私钥
    private static final String PRIVATE_KEY = "6d79264403d667f75caf2f1dca6412c6922b15433b515850c5c00a2aa12010e9";

    //节点服务
    private static NodeSrv nodeSrv;
    //合约地址
    private static String contractAddress;
    //交易哈希
    private static String existTxHash;
    private static PrivateKey privKey = new PrivateKeyECDSA(PRIVATE_KEY);




    @BeforeClass
    public static void init() throws IOException{
        nodeSrv = new NodeSrv("http://zgvm:46657");
        // 初始化contractAddress existTxHash
        String accountAddress = privKey.getAddress();
        //获取nonce
        int nonce = nodeSrv.queryNonce(accountAddress);
        //部署合约, 初始化contractAddress
        DeployContractResult deployRes = nodeSrv.deployContractCompl(bCode, Arrays.asList(), privKey, BigInteger.valueOf(nonce));
        contractAddress = deployRes.getContractAddr();
        log.info("contractAddr {}",contractAddress);

        nonce++;
        // event 定义
        Event DEPOSIT = new Event("Deposit",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Utf8String>() {
                }));
        Function functionDef = new Function("deposit", Arrays.asList(), Arrays.asList());
        existTxHash = nodeSrv.callContractEvm(BigInteger.valueOf(nonce),
                contractAddress,
                functionDef, //函数接口定义
                privKey,
                new DemoEventCallBack(DEPOSIT)); //callBack
        log.info("txHash {}" ,existTxHash);
    }



    @Test
    public void testDeployContract() throws IOException, InterruptedException {
        String accountAddress = privKey.getAddress();
        //获取nonce
        int nonce = nodeSrv.queryNonce(accountAddress);
        log.info("nonce {}" , nonce);
        //部署合约
        DeployContractResult deployRes = nodeSrv.deployContractCompl(bCode, Arrays.asList(), privKey, BigInteger.valueOf(nonce));
        log.info("contractAddr {}",deployRes.getContractAddr());
        log.info("deploy txHash {}",deployRes.getTxHash());
        Thread.sleep(1000);
    }


    @Test
    public void testQueryContract() throws IOException, InterruptedException {
        String address = privKey.getAddress();
        int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);
        Function functionDef = new Function("queryInfo", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        List<Type> resp  = nodeSrv.queryContract(BigInteger.valueOf(nonce), contractAddress, functionDef, privKey);
        log.info("resp {}", resp);
        Thread.sleep(5000);
    }

    @Test
    public void testQueryContractWithSig() throws IOException{
        String address = privKey.getAddress();
        int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);
        Function functionDef = new Function("queryInfo", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
        }));
        RawTransaction tx = TransactionUtil.createCallContractTransaction(BigInteger.valueOf(nonce), contractAddress, functionDef);
        Signature sig = CryptoUtil.generateSignature(tx, privKey);
        List<Type> resp  = nodeSrv.queryContractWithSig(BigInteger.valueOf(nonce), contractAddress, functionDef, sig);
        log.info("resp {}", resp);
    }

    @Test
    public void testCallContactWithSig() throws IOException, InterruptedException{
        String address = privKey.getAddress();
        int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);

        // event 定义
        Event DEPOSIT = new Event("Deposit",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Utf8String>() {
                }));

        Function functionDef = new Function("deposit", Arrays.asList(), Arrays.asList());
        RawTransaction tx = TransactionUtil.createCallContractTransaction(BigInteger.valueOf(nonce), contractAddress, functionDef);
        Signature sig = CryptoUtil.generateSignature(tx, privKey);
        String resp = nodeSrv.callContractEvmWithSig(BigInteger.valueOf(nonce),
                contractAddress,
                functionDef, //函数接口定义
                sig,
                new DemoEventCallBack(DEPOSIT)); //callBack

        log.info("call contract resp:" + resp);

        Thread.sleep(5000);
    }


    @Test
    public void testCallContactAsync() throws IOException, InterruptedException{

        /*
          参考test.sol
         */
        String address = privKey.getAddress();
        int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);

        // event 定义
        Event DEPOSIT = new Event("Deposit",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Utf8String>() {
                }));

        Function functionDef = new Function("deposit", Arrays.asList(), Arrays.asList());

        String resp = nodeSrv.callContractEvm(BigInteger.valueOf(nonce),
                contractAddress,
                functionDef, //函数接口定义
                privKey,
                new DemoEventCallBack(DEPOSIT), false); //callBack
        log.info("abc" + resp);
        Thread.sleep(10000);
    }




    @Test
    public void testQueryNonce() throws IOException {
        String address = privKey.getAddress();
        int resp = nodeSrv.queryNonce(address);
        log.info("testQueryNonce {}", resp);
    }

    @Test
    public void testQueryRecp() throws IOException {
        List<LogInfo> resp = nodeSrv.queryReceipt(existTxHash);
        Assert.assertNotNull(resp);
        log.info("" + resp);
    }

    @Test
    public void testRlp() {

        byte[] rlpEncoded = Numeric.hexStringToByteArray("80");
        RlpList decode = RlpDecoder.decode(rlpEncoded);
        String hexValue = ((RlpString) decode.getValues().get(0)).asString();
        Assert.assertEquals(hexValue, "0x");

        // event 定义
        Event DEPOSIT = new Event("Deposit",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Utf8String>() {
                }));

        byte[] rlp = Hex.decode("f9025f80825318b90100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000200000000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000020000000000000009453e952a018d2aa979ae15e82300e95621a47b644940000000000000000000000000000000000000000f90124f9012194ee463ba03224a14706728cbede6abcd76ed4e7cce1a04db2efd59f3e5a97173b6b255ce6ebaea50d7f27654cf39705afa269e2529b3ab8a0000000000000000000000000402c3035ec3ec9f320ffc4b20d22b1f39b6caddb000000000000000000000000402c3035ec3ec9f320ffc4b20d22b1f39b6caddb00000000000000000000000000000000000000000000000000000000000000600000000000000000000000000000000000000000000000000000000000000003616263000000000000000000000000000000000000000000000000000000000083118c30a046a2d7c505b3394f845061287a7630e106910b8c6a1e4d2185366eb870318deb80a000000000000000000000000025bd77dee7698dc5d730dadb00753dd1177cd87f8082531883014201");
        TransactionReceipt res = new TransactionReceipt(rlp);
        res.getLogInfoList().forEach(i -> {
            List<Type> respp = FunctionReturnDecoder.decode(Hex.toHexString(i.getData()), DEPOSIT.getNonIndexedParameters());
            Assert.assertEquals(respp.toString(), "[0x402c3035ec3ec9f320ffc4b20d22b1f39b6caddb, 0x402c3035ec3ec9f320ffc4b20d22b1f39b6caddb, abc]");
        });
    }
}
