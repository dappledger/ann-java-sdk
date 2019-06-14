# 众安链SDK
目前SDK支持智能合约的部署、调用和查询功能。

## 准备
编译solidity合约请自行完成<br/>
(可以通过 https://remix.ethereum.org/ 在线编译或使用solc本地编译<br/>
生成byteCode)
+ 编译 test.sol
``` shell
solc test.sol
```

## 使用说明（示例）
### 初始化节点服务
``` java
NodeSrv nodeSrv = new NodeSrv("http://${HOST}:${PORT}/");
```

### 生成账户私钥和地址
``` java 
String privateKey = CryptoUtil.generatePrivateKey();
String address = CryptoUtil.addressFromPrivkey(privateKey);
```
### 查询nonce 

``` java
Credentials credentials = Credentials.create(${PRIVATE_KEY});
String address = credentials.getAddress();
int nonce = nodeSrv.queryNonce(address);
```
### 部署合约
部署之前编译生成的byteCode到节点
``` java
String contractAddr = nodeSrv.deployContract(${byteCode}, Arrays.asList(), credentials, BigInteger.valueOf(nonce));
```

### 调用合约(默认同步调用)
``` java
// event 定义
Event DEPOSIT = new Event("Deposit", 
                Arrays.asList(), 
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Utf8String>() {
                }));

Function functionDef = new Function("deposit",  // function we're calling
            Arrays.asList(), // Parameters to pass as Solidity Types
            Arrays.asList()
            );

String resp = nodeSrv.CallContract(BigInteger.valueOf(nonce),
                "0xee463ba03224a14706728cbede6abcd76ed4e7cc",
                functionDef, //函数接口定义
                credentials,
                new DemoEventCallBack(DEPOSIT));
```
### 异步调用合约
```
    // 在调用callContract方法时指定isSync=false
    String resp = nodeSrv.CallContract(BigInteger.valueOf(nonce),
                    "0xee463ba03224a14706728cbede6abcd76ed4e7cc",
                    functionDef, //函数接口定义
                    credentials,
                    new DemoEventCallBack(DEPOSIT), false);
```
### 编写event 处理逻辑
EventCallBack通过轮询方式监听EVM events
``` java
import com.rendez.api.EventCallBack;
import lombok.extern.slf4j.Slf4j;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import java.util.List;
@Slf4j
public class DemoEventCallBack extends EventCallBack {

    /**
     * 
     * @param pollTime 轮询时间
     * @param event 监听事件的格式
     */
    public DemoEventCallBack(int pollTime, Event event) {
        super(pollTime, event);
    }

    public DemoEventCallBack(Event event) {
        super(event);
    }

    /**
     * 业务处理
     * @param decodeResult
     */
    @Override
    public void handleEvent(List<Type> decodeResult) {
        log.info("decoded result" + decodeResult);
        log.info("start to Process Data");
    }
}
```

### 查询合约
``` java
Credentials credentials = Credentials.create(PRIVATE_KEY);
String address = credentials.getAddress();
int nonce = nodeSrv.queryNonce(address);
log.info("nonce {}" , nonce);
Function functionDef = new Function("queryInfo", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
}));
List<Type> resp  = nodeSrv.queryContract(BigInteger.valueOf(nonce), contractAddress, functionDef, credentials);
log.info("resp {}", resp);
```



## 
### CryptoUtil类
#### addressFromPrivkey
```java
static String generatePrivateKey()
```
生成私钥

#### addressFromPrivkey
```java
static String addressFromPrivkey(String privKey)
```
根据私钥生成账户地址address

#### addressFromPrivkey
```java
static boolean verifySignature(String txMsg, String address)
```
验证交易签名是否有效

#### verifySignature
```java
static boolean verifySignature(String txMsg, String address)
```
验证交易是否签名有效

### TransactionUtil类
#### decodeTxMsg
```java
static SignedRawTransaction decodeTxMsg(String txMsg)
```
decode链上交易, 返回交易详细信息

### NodeSrv类
#### queryNonce
```java
Integer queryNonce(String address)
```
根据账户地址查询nonce

#### queryReceipt
```java
List<LogInfo> queryReceipt(String txHash)
```
查询交易执行event log

#### queryReceiptRaw
```java
TransactionReceipt queryReceiptRaw(String txHash)
```
查询交易执行receipt

#### queryContract
```java
List<Type> queryContract(BigInteger nonce, String contractAddress, Function function, Credentials credential)
```
查询合约

#### queryContractWithSig
```java
queryContractWithSig(BigInteger nonce, String contractAddress, Function function, Sign.SignatureData sig)
```
签名查询合约

#### callContractEvm
```java
String callContractEvm(BigInteger nonce, String contractAddress, Function function, Credentials credential, EventCallBack callBack, boolean isSyncCall)
```
调用evm 合约

#### batchCallContractEvm
```java
List<String> batchCallContractEvm(List<byte[]> txs)
```
批量调用合约，返回交易哈希列表

#### deployContract
```java
String deployContract(String binaryCode, List<Type> constructorParameters, Credentials credentials, BigInteger nonce)
```
部署合约，返回合约地址

#### blockHashs
通过块hash查询块中有效交易
```java
BlockHashResult blockHashs(String blockHash)
```