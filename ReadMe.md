# 众安链SDK
目前SDK支持智能合约的部署、调用和查询功能。

## 准备
编译solidity合约请自行完成<br/>
(可以通过https://remix.ethereum.org/在线编译或使用solc本地编译<br/>
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

### 生成账户私钥和地址(支持PrivateKeyECDSA)
``` java
PrivateKey privKey = PrivateKeyECDSA.Create();
String privKeyStr = privKey.toHexString();
String address = privKey.getAddress();
```

### 从已有私钥16进制字符初始化私钥对象
``` java
PrivateKey privKey = new PrivateKeyECDSA("0x6d79264403d667f75caf2f1dca6412c6922b15433b515850c5c00a2aa12010e9");
String address = privKey.getAddress();
```
### 查询nonce

``` java
PrivateKey privKey;
String address = privKey.getAddress();
int nonce = nodeSrv.queryNonce(address);
```
### 部署合约
部署之前编译生成的byteCode到节点

``` java
PrivateKey privKey = new PrivateKeyECDSA(${privKeyStr});
String contractAddr = nodeSrv.deployContract(${byteCode}, Arrays.asList(), privKey, BigInteger.valueOf(nonce));
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
                privKey,
                new DemoEventCallBack(DEPOSIT));
```
### 异步调用合约
```
    // 在调用callContract方法时指定isSync=false
    String resp = nodeSrv.CallContract(BigInteger.valueOf(nonce),
                    "0xee463ba03224a14706728cbede6abcd76ed4e7cc",
                    functionDef, //函数接口定义
                    privKey,
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
PrivateKey privKey = new PrivateKeyECDSA(${privKeyStr});
String address = privKey.getAddress();
int nonce = nodeSrv.queryNonce(address);
log.info("nonce {}" , nonce);
Function functionDef = new Function("queryInfo", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
}));
List<Type> resp  = nodeSrv.queryContract(BigInteger.valueOf(nonce), contractAddress, functionDef, privKey);
log.info("resp {}", resp);
```

### 根据块高度查询合约
``` java
PrivateKey privKey = new PrivateKeyECDSA(${privKeyStr});
String address = privKey.getAddress();
int nonce = nodeSrv.queryNonce(address);
int height = 100;
log.info("nonce {}" , nonce);
Function functionDef = new Function("queryInfo", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {
}));
List<Type> resp  = nodeSrv.queryContractByHeight(BigInteger.valueOf(nonce), contractAddress, functionDef, privKey, BigInteger.valueOf(height));
log.info("resp {}", resp);
```

### 根据块高度查询交易hash列表
``` java
int height = 100;
String[] resp  = nodeSrv.queryTransactionHashsByHeight(BigInteger.valueOf(height));
log.info("resp {}", resp);
```

### 根据交易hash查询交易数据
``` java
String txhash = "";
RawTransactionData resp  = nodeSrv.queryRawTransactionByHash(txhash);
log.info("resp {}", resp);
```


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

#### queryRawTransactionByHash
```java
RawTransactionData queryRawTransactionByHash(String txhash)
```
根据交易hash查询交易数据

#### queryTransactionHashsByHeight
```java
String[] queryTransactionHashsByHeight(BigInteger height)
```
根据块高度查询交易hash列表

#### deployContract
```java
String deployContract(String binaryCode, List<Type> constructorParameters, PrivateKey privKey, BigInteger nonce)
```
部署合约，返回合约地址

#### callContractEvm
```java
String callContractEvm(BigInteger nonce, String contractAddress, Function function, PrivateKey privKey, EventCallBack callBack, boolean isSyncCall)
```
调用evm 合约

#### queryContract
```java
List<Type> queryContract(BigInteger nonce, String contractAddress, Function function, PrivateKey privKey)
```
查询合约

#### queryContractByHeight
```java
List<Type> queryContractByHeight(BigInteger nonce, String contractAddress, Function function, PrivateKey privKey, BigInteger height)
```
指定区块高度查询合约

#### queryContractWithSig
```java
queryContractWithSig(BigInteger nonce, String contractAddress, Function function, Signature sig)
```
签名查询合约

#### queryReceiptRaw
```java
TransactionReceipt queryReceiptRaw(String txHash)
```
查询交易执行receipt

#### queryReceipt
```java
List<LogInfo> queryReceipt(String txHash)
```
查询交易执行event log
