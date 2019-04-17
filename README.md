# AnnChain-java-sdk

## sdk-demo

### 1. generate address

```java
 @Test
 public void generatAddress(){
    ECKeyPair keypair = null;
    try {
    keypair = Keys.createEcKeyPair();
    } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
     e.printStackTrace();
    }
    byte[] pri = keypair.getPrivateKey().toByteArray();
    byte[] pub = keypair.getPublicKey().toByteArray();
    String address = Keys.getAddress(keypair);
    Credentials credentials = Credentials.create(ByteUtilities.toHexString(pri));
    Assert.assertEquals("0x"+address,credentials.getAddress());
}
```

### 2. create account

```java
@Test
public void testCreateAccount(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");
    CreateAccount createAccount = new CreateAccount();
    createAccount.setBasefee("0");
    createAccount.setNonce("0");
    createAccount.setMemo("create account");
    createAccount.setStartingBalance("10000000000");
    createAccount.setFrom(credentials.getAddress());
    createAccount.setTo("0x1118a901790717520143808672e97d518fef87ea");
    Result result = anChainGateWay.createAccount(createAccount, credentials);
    Assert.assertNotNull(result.getTxHash());
    Assert.assertNull(result.getError().getCode());
}
```

### 3. query account Info

```java
@Test
public void queryAccountInfo(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    AccountResult result = anChainGateWay.queryAccountInfo("0x1118a901790717520143808672e97d518fef87ea");
    Assert.assertNotNull(result.getAccount());
}
```

### 4. payment

```java
@Test
public void testPayment(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");
    Payment payment = new Payment();
    payment.setNonce("0");
    payment.setMemo("111");
    payment.setAmount("999");
    payment.setBasefee("100");
    payment.setFrom(credentials.getAddress());
    payment.setTo("0x76f64caecfc23a5923efbb5f6b692b91a70aa429");
    Result result = anChainGateWay.payment(payment,credentials);
    Assert.assertNull(result.getError().getCode());
}
```

### 5. manager data

```java
@Test
public void testManagerData(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");
    ManagerData managerData = new ManagerData();
    Map<String,Value> keyPair1 = new HashMap<>();
    Value value = new Value();
    value.setCategory("E");
    value.setValue("value1");
    keyPair1.put("name3",value);
    Value value2 = new Value();
    value2.setCategory("F");
    value2.setValue("value2");
    keyPair1.put("name4",value2);
    managerData.setKeyPairList(keyPair1);
    managerData.setKeyPairList(keyPair1);
    managerData.setBasefee("100");
    managerData.setFrom(credentials.getAddress());
    managerData.setNonce("1");
    managerData.setMemo("memo");
    Result result = anChainGateWay.managerData(managerData,credentials);
    Assert.assertNull(result.getError().getCode());
    Assert.assertNotNull(result.getTxHash());
}
```

### 6. create contact

```java
 @Test
 public void createContract(){
     AnChainGateWay anChainGateWay = getClient("http://localhost:46657/");
     CreateContract createContract = new CreateContract();
     Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");
     createContract.setBasefee("10");
     createContract.setNonce("12");
     createContract.setFrom("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");
     createContract.setMemo("");
     createContract.setNonce("5");
     createContract.setAmount("0");
     createContract.setGas_limit("8000000");
     createContract.setGas_price("0");
        createContract.setContractCode("608060405234801561001057600080fd5b506009600190815560028054600160a060020a03191633179055600019600355600860009081556040805180820190915260048082527f6b656c65000000000000000000000000000000000000000000000000000000006020909201918252919261007d9290919061010a565b50600280548282018054600160a060020a031916600160a060020a0390921691909117905581546004805460ff191660ff9092169190911781556001838101805485946100de93600593926000199181161561010002919091011604610188565b5060029182015491018054600160a060020a031916600160a060020a039092169190911790555061021a565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061014b57805160ff1916838001178555610178565b82800160010185558215610178579182015b8281111561017857825182559160200191906001019061015d565b506101849291506101fd565b5090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101c15780548555610178565b8280016001018555821561017857600052602060002091601f016020900482015b828111156101785782548255916001019190600101906101e2565b61021791905b808211156101845760008155600101610203565b90565b6104ed806102296000396000f3006080604052600436106100ae5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166330650a3281146100b357806335b873cd146100dd57806336142497146101925780637cea649d146101bd5780639b5c7961146101d25780639cad65241461025c5780639d4853c414610274578063a36000d31461028c578063ac9d8b1e146102a1578063b2bdfa7b146102df578063c0abc010146102f4575b600080fd5b3480156100bf57600080fd5b506100cb600435610309565b60408051918252519081900360200190f35b3480156100e957600080fd5b506100f2610311565b6040805160ff8516815273ffffffffffffffffffffffffffffffffffffffff831691810191909152606060208083018281528551928401929092528451608084019186019080838360005b8381101561015557818101518382015260200161013d565b50505050905090810190601f1680156101825780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b34801561019e57600080fd5b506101a76103c8565b6040805160ff9092168252519081900360200190f35b3480156101c957600080fd5b506100cb6103d1565b3480156101de57600080fd5b506101e76103d7565b6040805160208082528351818301528351919283929083019185019080838360005b83811015610221578181015183820152602001610209565b50505050905090810190601f16801561024e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561026857600080fd5b506100cb60043561046d565b34801561028057600080fd5b506100cb600435610475565b34801561029857600080fd5b506100cb61047d565b3480156102ad57600080fd5b506102b6610483565b6040805173ffffffffffffffffffffffffffffffffffffffff9092168252519081900360200190f35b3480156102eb57600080fd5b506102b661049f565b34801561030057600080fd5b506100cb6104bb565b600381905590565b600480546005805460408051602060026101006001861615026000190190941693909304601f810184900484028201840190925281815260ff909416949392918301828280156103a25780601f10610377576101008083540402835291602001916103a2565b820191906000526020600020905b81548152906001019060200180831161038557829003601f168201915b5050506002909301549192505073ffffffffffffffffffffffffffffffffffffffff1683565b60045460ff1690565b60035481565b60058054604080516020601f60026000196101006001881615020190951694909404938401819004810282018101909252828152606093909290918301828280156104635780601f1061043857610100808354040283529160200191610463565b820191906000526020600020905b81548152906001019060200180831161044657829003601f168201915b5050505050905090565b600181905590565b600081905590565b60015481565b60065473ffffffffffffffffffffffffffffffffffffffff1690565b60025473ffffffffffffffffffffffffffffffffffffffff1681565b600054815600a165627a7a72305820dc2f4e4b2d1cdc5ec7830c60152ea0d7e4203b44af3b1fb692aea3511ed8291c0029");
     Result result = anChainGateWay.createContract(createContract,credentials);
     Assert.assertNull(result.getError().getCode());
     Assert.assertNotNull(result.getTxHash());
 }
```

### 7. query nonce

```java
 @Test
 public void  testQueryNonce(){
     AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
     NonceResult nonceResult = anChainGateWay.queryNonce("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");
     Assert.assertNotNull(nonceResult.getNonce());
    }
```

### 8. query ledger paper

```java
 @Test
 public void queryLedgerPaper(){
     AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
     Pagination pagination = new Pagination();
     LedgerPagerListResult result = anChainGateWay.queryLedgerPaper(pagination);
     Assert.assertNull(result.getError().getCode());
 }
```

### 9. query ledger by sequence

```java
 @Test
 public void queryLedgerBySequence(){
     AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
     LedgerPagerResult result = anChainGateWay.queryLedgerBySequence(229L);
     Assert.assertNull(result.getError().getCode())
 }
```

### 10. query all trans

```java
 @Test
 public void queryAllTrans(){
     AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
     Pagination pagination = new Pagination();
     TransResultList result = anChainGateWay.queryAllTrans(pagination);
     Assert.assertNull(result.getError().getCode());
 }
```

### 11. query trans by account

```java
@Test
 public void queryTransByAccount(){
     AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
     QueryTrans queryTransReq = new QueryTrans();
     queryTransReq.setAddress("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");
     TransResultList result = anChainGateWay.queryTransByAccount(queryTransReq);
     Assert.assertNull(result.getError().getCode());
 }
```

### 12. query trans by txHash

```java
@Test
public void queryTransByTxHash(){
   AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
   TransResult result = anChainGateWay.queryTransByTxHash("0x74d1597923341f71213ac6fe912e0c9efdcad22bf49cd85ad2e9005876940bd9");
   Assert.assertNull(result.getError().getCode());
}
```

### 13. query all trades

```java
@Test
public void queryAllTrades(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    Pagination pagination = new Pagination();
    TradeResultList result = anChainGateWay.queryAllTrades(pagination);
    Assert.assertNull(result.getError().getCode());
}
```

### 14. query all trades by hash

```java
@Test
public void queryAllTradesByTxhash(){
   AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
   TradeResult tradeResult = anChainGateWay.queryTradesByTxHash("0x60c0ff6c7dc84cf2d35aa3ad0b7c395dce0fa6ac4426b80bb3ff08ae32407d2f");
   Assert.assertNull(tradeResult.getError().getCode());
}
```

### 15. query all trades by address

```java
 @Test
 public void queryAllTradesByAddress(){
     AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
     QueryTrades queryLedgerReq = new QueryTrades();
     queryLedgerReq.setAddress("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");
     TradeResultList tradeResult = anChainGateWay.queryTradesByAccount(queryLedgerReq);
     Assert.assertNull(tradeResult.getError().getCode());
 }
```

### 16. query all trades by tx

```java
@Test
public void queryAllLedgerByTx(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    QueryLedger queryLedgerReq = new QueryLedger();
    queryLedgerReq.setHeight(76l);
    TradeResultList tradeResult = anChainGateWay.queryLedgerTradesByHeight(queryLedgerReq);
    Assert.assertNull(tradeResult.getError().getCode());
}
```

### 17. query manager data

```java
@Test
public void queryManagerData(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    QueryTrans queryTransReq = new QueryTrans();
    queryTransReq.setAddress("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");
    Result<Map<String,Value>> result = anChainGateWay.queryManagerData(queryTransReq);
    Assert.assertNull(result.getError().getCode());
}
```

### 18. query manager data by key

```java
@Test
public void queryManagerDataByKey(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    List<String> key = new ArrayList<>();
    key.add("1");
    Result<Map<String,Value>> result = anChainGateWay.queryManagerDataByKey
                ("0x65188459a1dc65984a0c7d4a397ed3986ed0c853","name1");
    Assert.assertNull(result.getError().getCode());
}
```

### 19. query manager data by category

```java
@Test
public void queryManagerDataByCategory(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    Result<Map<String,Value>> result = anChainGateWay.queryManagerDataByCategory
                ("0x65188459a1dc65984a0c7d4a397ed3986ed0c853","B");
    Assert.assertNull(result.getError().getCode());
}
```

### 20. query contract receipt

```java
@Test
public void queryContractReceipt(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    ContractReceiptResult receiptResult = anChainGateWay.
            queryContractReceipt("0x247bbe07be09881e74c6f7f7393fda1ae3cdde487fdd3696d8ee2329acd58a95");
    ContractReceipt contractReceipt = receiptResult.getResult();
    Assert.assertNull(receiptResult.getError().getCode());
}
```

### 21. query contract

```java
@Test
public void queryContract(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");
    GetContractResult getContractResult = new GetContractResult();
    //Address adress = new Address("0x76f64caecfc23a5923efbb5f6b692b91a70aa429");
    List<Type> inputArgs = new ArrayList<>();
    //inputArgs.add(adress);
    getContractResult.setInputArgs(inputArgs);
    List<TypeReference<Type>> outputt = new ArrayList<>();
    List<TypeReference<?>> outPutArgs = new ArrayList<>();
    TypeReference output = new TypeReference<Int256>() {};
    outputt.add(output);
    outPutArgs.add(output);
    getContractResult.setOutputArgs(outPutArgs);
    getContractResult.setMethod_name("_numC");
    getContractResult.setFrom("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");
    getContractResult.setTo("0x3c3be41333d48c72b8ad7d0c02bb188faaec3815");
    ContractResult contractResult =     anChainGateWay.queryContract(getContractResult,credentials);
    Assert.assertNotNull(contractResult.getContractResult(outputt).get(0).getValue());
}
```

### 22. execute contract 

```java
@Test
public void  executeContractTest(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    ContractTrans contractTrans = new ContractTrans();
    Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");
    contractTrans.setAmount("0");
    contractTrans.setGas_limit("8000000");
    contractTrans.setGas_price("0");
    List<Type> inputArgs = new ArrayList<>();
    //Address adress = new Address("0x76f64caecfc23a5923efbb5f6b692b91a70aa429");
    Int256 uint8 = new Int256(-1);
    //inputArgs.add(adress);
    inputArgs.add(uint8);
    contractTrans.setInputArgs(inputArgs);
    List<TypeReference<?>> outPutArgs = new ArrayList<>();
    TypeReference output = new TypeReference<Int256>() {};
    outPutArgs.add(output);
    contractTrans.setOutputArgs(outPutArgs);
    contractTrans.setMethod_name("testC");
    contractTrans.setBasefee("10");
    contractTrans.setFrom(credentials.getAddress());
    contractTrans.setTo("0x3c3be41333d48c72b8ad7d0c02bb188faaec3815");
    contractTrans.setMemo("");
    contractTrans.setNonce("8");
    Result result =  anChainGateWay.executeContract(contractTrans,credentials,null);
    /*****************event if exists ***********************************/
    EventCallBack eventCallBack = new EventCallBack() {
            @Override
            public void eventCall(String eventName, List<Type> values) {
                System.out.println("### bussiness code ############eventName= "+eventName + "values[0]="+values.get(0).getValue());
            }
        };
    List<TypeReference<Type>> eventType = new ArrayList<>();
    TypeReference output2 = new TypeReference<Address>() {};
    TypeReference output3 = new TypeReference<Address>() {};
    TypeReference output4 = new TypeReference<Utf8String>() {};
    eventType.add(output2);
    eventType.add(output3);
    eventType.add(output4);
    EventCallBack.EventVo eventVo = eventCallBack.new EventVo("Deposit",eventType);
    eventCallBack.addEvents(eventVo);
    /*****************event if exists ***********************************/
    Result result =  anChainGateWay.executeContract(contractTrans,credentials,eventCallBack);
    Assert.assertNotNull(result.getTxHash());
    Assert.assertNull(result.getError().getCode());
}
```

### 23. query contract status

```java
@Test
public void queryConstractStatus(){
    AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
    ContactExistResult contactExist = anChainGateWay.queryContractStatus("0x9458f9c16f2e6b1013793d55b8a48fa4e736ac02");
    Assert.assertNull(contactExist.getError().getCode());
}
```

### 24.  get client

```java
public static AnChainGateWay getClient(String url) {
    return new AnChainGateWay(new HttpService(url));
}
```

