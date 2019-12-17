
import com.genesis.api.CryptoUtil;
import com.genesis.api.NodeSrv;
import com.genesis.api.TransactionUtil;
import com.genesis.api.bean.model.DeployContractResult;
import com.genesis.api.bean.model.QueryBlockTransactionHashs;
import com.genesis.api.bean.model.QueryKVResult;
import com.genesis.api.bean.model.QueryPrefixKVs;
import com.genesis.api.bean.model.QueryTransactionPayload;
import com.genesis.api.bean.model.QueryTransactionReceipt;
import com.genesis.api.bean.model.RawTransactionData;
import com.genesis.api.bean.model.SendTransactionResult;
import com.genesis.api.bean.model.TransactionKVTx;
import com.genesis.api.crypto.PrivateKey;
import com.genesis.api.crypto.PrivateKeyECDSA;
import com.genesis.api.crypto.Signature;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
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
    private static final String bCode = "608060405234801561001057600080fd5b50610397806100206000396000f3fe608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632c560ec01461005c5780635d46732614610087578063f1215d25146100ec575b600080fd5b34801561006857600080fd5b506100716101be565b6040518082815260200191505060405180910390f35b34801561009357600080fd5b506100d6600480360360208110156100aa57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610204565b6040518082815260200191505060405180910390f35b3480156100f857600080fd5b506101bc6004803603604081101561010f57600080fd5b81019080803590602001909291908035906020019064010000000081111561013657600080fd5b82018360208201111561014857600080fd5b8035906020019184600183028401116401000000008311171561016a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061024c565b005b60008060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905090565b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b816000803373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055507f643e927b32d5bfd08eccd2fcbd97057ad413850f857a2359639114e8e8dd3d7b338383604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200183815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561032b578082015181840152602081019050610310565b50505050905090810190601f1680156103585780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a1505056fea165627a7a7230582013df8e0098a02a38849c352af9c092166e0876d055eb7c2f976fdf21e75a849b0029";
    //私钥
    private static final String PRIVATE_KEY = "6d79264403d667f75caf2f1dca6412c6922b15433b515850c5c00a2aa12010e9";
    private static final String PRIVATE_KEY2 = "37F1055CFD87F5B071AA6293DA4C8B79DB57949C07CD87BA20493A0C9D254DE9";
    //节点服务
    private static NodeSrv nodeSrv;
    //合约地址
    private static String contractAddress;
    //交易哈希
    private static String existTxHash;
    //交易块高度
    private static BigInteger blockHeight;
    private static PrivateKey privKey = new PrivateKeyECDSA(PRIVATE_KEY);
    private static PrivateKey privKey2 = new PrivateKeyECDSA(PRIVATE_KEY2);

    @BeforeClass
    public static void init() throws IOException{	
        nodeSrv = new NodeSrv("http://localhost:46657");
    }
    
    @Test
    public void test() throws IOException, InterruptedException, CryptoException{
    	testQueryNonce();
    	
    	// contract
    	testDeployContract();
    	testCallContactWithSig();
    	testCallContactAsync();
    	testQueryContract();
    	testQueryContractWithSig();
    	testQueryRecp();
    	testRlp();
    	testQueryTxHashsByHeight();
    	testQueryRawTransactionByHash();
    	
    	// KV 
    	testPutKV();
    	queryKV();
    	testPutKVAsync();
    	queryKV2();
    	queryKVNotExist();
    	testPutKVs();
    	queryPreKV();
    	queryPreKV2();
    	
    	// payload
    	testSendPayload();
    }

    // @Test
    public void testDeployContract() throws IOException, InterruptedException {
        String accountAddress = privKey.getAddress();
        //获取nonce
        int nonce = nodeSrv.queryNonce(accountAddress);
        log.info("nonce {}" , nonce);
        //部署合约
        DeployContractResult deployRes = nodeSrv.deployContractCompl(bCode, Arrays.asList(), privKey, BigInteger.valueOf(nonce));
        contractAddress = deployRes.getContractAddr();
        log.info("contractAddr {}",contractAddress);
        log.info("deploy txHash {}",deployRes.getTxHash());
        Thread.sleep(2000);
    }
    
    // @Test
    public void testCallContactWithSig() throws IOException, InterruptedException, CryptoException {
        String address = privKey.getAddress();
        int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);

        // event 定义
        Event DEPOSIT = new Event("deposit",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Uint>() {
                }, new TypeReference<Utf8String>() {
                }));

        Function functionDef = new Function("deposit", 
        		Arrays.asList(
        		new org.web3j.abi.datatypes.generated.Uint256(18),
        		new org.web3j.abi.datatypes.Utf8String("test1")), 
        		Arrays.asList());
        RawTransaction tx = TransactionUtil.createCallContractTransaction(BigInteger.valueOf(nonce), contractAddress, functionDef);
        Signature sig = CryptoUtil.generateSignature(tx, privKey);
        String resp = nodeSrv.callContractEvmWithSig(BigInteger.valueOf(nonce),
                contractAddress,
                functionDef, //函数接口定义
                sig,
                new DemoEventCallBack(DEPOSIT)); //callBack
        existTxHash = resp;
        log.info("call contract resp with sig:" + resp);

        Thread.sleep(2000);
    }


    // @Test
    public void testCallContactAsync() throws IOException, InterruptedException, CryptoException {

        /**
         * 参考test.sol
         */
        String address2 = privKey2.getAddress();
        int nonce = nodeSrv.queryNonce(address2);
        log.info("nonce {}" , nonce);

        // event 定义
        Event DEPOSIT = new Event("Deposit",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Uint>() {
                }, new TypeReference<Utf8String>() {
                }));

        Function functionDef = new Function("deposit", 
        		Arrays.asList(
                new org.web3j.abi.datatypes.generated.Uint256(19),
                new org.web3j.abi.datatypes.Utf8String("test2")), 
                Arrays.asList());

        String resp = nodeSrv.callContractEvm(BigInteger.valueOf(nonce),
                contractAddress,
                functionDef, //函数接口定义
                privKey2,
                new DemoEventCallBack(DEPOSIT), false); //callBack
        log.info("call contract resp:" + resp);
        
        Thread.sleep(2000);
    }

    // @Test
    public void testQueryContract() throws IOException, InterruptedException {
        String address = privKey.getAddress();
        int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);
        Function functionDef = new Function("queryInfo", 
        		Arrays.asList(), 
        		Arrays.asList(new TypeReference<Uint256>() {
        		}));
        List<Type> resp  = nodeSrv.queryContract(BigInteger.valueOf(nonce), contractAddress, functionDef, privKey);
        log.info("query resp {}", resp.get(0).getValue());
        Thread.sleep(2000);
    }

    // @Test
    public void testQueryContractWithSig() throws IOException, CryptoException {
        String address2 = privKey2.getAddress();
        int nonce = nodeSrv.queryNonce(address2);
        log.info("nonce {}" , nonce);
        Function functionDef = new Function("queryInfoWithAddress", 
        		Arrays.asList(new org.web3j.abi.datatypes.Address(address2)), 
        		Arrays.asList(new TypeReference<Uint256>() {
        }));
        RawTransaction tx = TransactionUtil.createCallContractTransaction(BigInteger.valueOf(nonce), contractAddress, functionDef);
        Signature sig = CryptoUtil.generateSignature(tx, privKey2);
        List<Type> resp  = nodeSrv.queryContractWithSig(BigInteger.valueOf(nonce), contractAddress, functionDef, BigInteger.valueOf(0), sig);
        log.info("query resp2 {}", resp.get(0).getValue());
    }

    // @Test
    public void testQueryNonce() throws IOException {
        String address = privKey.getAddress();
        int resp = nodeSrv.queryNonce(address);
        log.info("testQueryNonce {}", resp);
    }

    // @Test
    public void testQueryRecp() throws IOException {
    	log.info("receipt_hash:" + existTxHash);
    	QueryTransactionReceipt resp = nodeSrv.queryReceiptRaw(existTxHash);
        Assert.assertNotNull(resp);
        blockHeight = resp.getHeight();
        log.info("receipt:" + resp);
    }

    // @Test
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
        QueryTransactionReceipt res = new QueryTransactionReceipt(rlp);
        res.getLogInfoList().forEach(i -> {
            List<Type> respp = FunctionReturnDecoder.decode(Hex.toHexString(i.getData()), DEPOSIT.getNonIndexedParameters());
            Assert.assertEquals(respp.toString(), "[0x402c3035ec3ec9f320ffc4b20d22b1f39b6caddb, 0x402c3035ec3ec9f320ffc4b20d22b1f39b6caddb, abc]");
        });
    }
    	
    // @Test
    public void testQueryTxHashsByHeight() throws IOException {
    	QueryBlockTransactionHashs txHashs = nodeSrv.queryTransactionHashsByHeight(blockHeight);
        log.info("count:"+txHashs.getCount());
        for (int i =0;i <txHashs.getHashs().length;i++) {
        	log.info("hash:"+txHashs.getHashs()[i]);
        }
    }
    
    // @Test
    public void testQueryRawTransactionByHash() throws IOException {
    	RawTransactionData rawTx = nodeSrv.queryRawTransactionByHash(existTxHash);
        log.info("to={},nonce={},amout={},gaslimit={},gasprice={},input={},v={},r={},s={}",Hex.toHexString(rawTx.getTo()),rawTx.getNonce(),rawTx.getValue(),rawTx.getGas(),rawTx.getGasprice(),rawTx.getInput(),rawTx.getV(),rawTx.getR(),rawTx.getS());
    }
    
    
    // @Test
    public void testPutKV() throws IOException, InterruptedException, CryptoException {
    	String address = privKey.getAddress();
    	int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);
        
        SendTransactionResult res = nodeSrv.putKv(BigInteger.valueOf(nonce), "key101", "value101", privKey);
        log.info("put kv sync res hash:"+ res.getTxHash() + ";error:"+ res.getErrMsg());
        Thread.sleep(2000);
    }
    
    // @Test
    public void queryKV() throws IOException, InterruptedException, CryptoException {
    	QueryKVResult res = nodeSrv.getKvValueWithKey("key101");
        log.info("get kv's value:" + res.getValue()+ ";error:"+res.getErrMsg());
    }
    
    
    // @Test
    public void testPutKVAsync() throws IOException, InterruptedException, CryptoException {
    	String address = privKey.getAddress();
    	int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);
        
        SendTransactionResult res  = nodeSrv.putKvAsync(BigInteger.valueOf(nonce), "key101", "value102", privKey);
        log.info("put kv asyc res hash:"+ res.getTxHash() + ";error:"+ res.getErrMsg());
        Thread.sleep(2000);
    }
    
    // @Test
    public void queryKV2() throws IOException, InterruptedException, CryptoException {
    	QueryKVResult res = nodeSrv.getKvValueWithKey("key101");
    	log.info("get kv's value2:" + res.getValue()+ ";error:"+res.getErrMsg());
    }
    
    // @Test
    public void queryKVNotExist() throws IOException, InterruptedException, CryptoException {
    	QueryKVResult res = nodeSrv.getKvValueWithKey("key102");
    	log.info("get kv's value2:" + res.getValue()+ ";error:"+res.getErrMsg());
    }
    
    // @Test
    public void testPutKVs() throws IOException, InterruptedException, CryptoException {
    	String address = privKey.getAddress();
    	int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);
        
        SendTransactionResult res1  = nodeSrv.putKv(BigInteger.valueOf(nonce), "key102", "value102", privKey);
        log.info("put kv sync res1 hash:"+ res1.getTxHash() + ";error:"+ res1.getErrMsg());
        SendTransactionResult res2  = nodeSrv.putKv(BigInteger.valueOf(nonce+1), "key103", "value103", privKey);
        log.info("put kv sync res2 hash:"+ res2.getTxHash() + ";error:"+ res2.getErrMsg());
        Thread.sleep(2000);
    }
    
    // @Test
    public void queryPreKV() throws IOException, InterruptedException, CryptoException {
    	QueryPrefixKVs res = nodeSrv.getKvValueWithPrefix("k","key101",BigInteger.valueOf(2));
    	if (res.getErrMsg() != null){
    		log.info("get prefix kvs's key error:"+res.getErrMsg());
    	}else {
    		for (int i =0;i < res.getTxs().length;i++) {
    			TransactionKVTx kv = res.getTxs()[i];
        		log.info("get prefix kvs's key:"+kv.getKey() + ";value:" + kv.getValue());
        	}
    	}
    }
    
    // @Test
    public void queryPreKV2() throws IOException, InterruptedException, CryptoException {
    	QueryPrefixKVs res = nodeSrv.getKvValueWithPrefix("k","key104",BigInteger.valueOf(2));
    	if (res.getErrMsg() != null){
    		log.info("get prefix kvs's key error:"+res.getErrMsg());
    	}else {
    		for (int i =0;i < res.getTxs().length;i++) {
    			TransactionKVTx kv = res.getTxs()[i];
        		log.info("get prefix kvs's key:"+kv.getKey() + ";value:" + kv.getValue());
        	}
    	}
    }
    
    
    // @Test
    public void testSendPayload() throws IOException, InterruptedException, CryptoException {
    	String address = privKey.getAddress();
    	int nonce = nodeSrv.queryNonce(address);
        log.info("nonce {}" , nonce);
        
        SendTransactionResult res  = nodeSrv.sendPayloadTx(BigInteger.valueOf(nonce), null, "payload2", BigInteger.valueOf(0), privKey);
        log.info("send payload sync res hash:"+ res.getTxHash() + ";error:"+ res.getErrMsg());
        Thread.sleep(2000);
        
        QueryTransactionPayload pay = nodeSrv.getPayloadWithHash(res.getTxHash());
        log.info("getpayload value:" + pay.getPayload()+ ";error:"+pay.getErrMsg());
    }
}
