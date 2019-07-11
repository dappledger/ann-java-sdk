# 基于众安链 blockdb-SDK
基于区块链共识算法的kv存储系统
####（1）防篡改，带有默克尔证明的区块式数据存储，数据防篡改；
####（2）可追溯，全量存储Add操作原始数据，任何写入行为都可被追溯并证明；
####（3）用户识别，数据库操作需加签验签用于用户操作识别；
####（4）KV存储，数据支持KV键值对数据存储；
####（5）支持SLF4j的append数据源插件

## 使用说明（示例）
### 初始化节点服务
``` java
NodeSrv nodeSrv = new NodeSrv("http://${HOST}:${PORT}/");
```


## 
### PrivateKey类
#### create 生成私钥
```java
static PrivateKey Create()
```

### NodeSrv类
#### blockdbPuter blockdb数据提交
```java
public String blockdbPuter(String privateKey, byte[] value, boolean isAsyn)
```

#### blockdbGeter blockdb数据查询
```java
public BlockDbResult blockdbGeter(String txHash)
```
查询交易执行event log

#### blockHashs 通过块hash查询有效交易
```java
public BlockHashResult blockHashs(String hash)
```
