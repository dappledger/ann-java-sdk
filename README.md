# AnnChain-java-sdk
介绍：AnnChain java sdk库提供了构建交易和连接到底层链Genesis的API

Demo :

 基础请求字段：BaseRequest { basefee,memo,from,to,nonce } 不能为空！
 

1, generate address<br>
   &nbsp;&nbsp;@Test<br>
    &nbsp;&nbsp;public void generatAddress(){<br>
        &nbsp;&nbsp;&nbsp;&nbsp;ECKeyPair keypair = null;<br>
        &nbsp;&nbsp;&nbsp;&nbsp;try {<br>
            &nbsp;&nbsp;&nbsp;&nbsp;keypair = Keys.createEcKeyPair();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {<br>
           &nbsp;&nbsp;&nbsp;&nbsp; e.printStackTrace();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;}<br>
        &nbsp;&nbsp;&nbsp;&nbsp;byte[] pri = keypair.getPrivateKey().toByteArray();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;byte[] pub = keypair.getPublicKey().toByteArray();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;String address = Keys.getAddress(keypair);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Credentials credentials = Credentials.create(ByteUtilities.toHexString(pri));<br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Assert.assertEquals("0x"+address,credentials.getAddress());<br>
    }<br>
2, create account<br>
   &nbsp;&nbsp; @Test<br>
    &nbsp;&nbsp;public void testCreateAccount(){<br>
        &nbsp;&nbsp;&nbsp;&nbsp;AnChainGateWay anChainGateWay = getClient("http://URL:46657/");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");<br>
       &nbsp;&nbsp;&nbsp;&nbsp; CreateAccount createAccount = new CreateAccount();<br>
       &nbsp;&nbsp;&nbsp;&nbsp; createAccount.setBasefee("0");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;createAccount.setNonce("0");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;createAccount.setMemo("create account");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;createAccount.setStartingBalance("10000000000");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;createAccount.setFrom(credentials.getAddress());<br>
        &nbsp;&nbsp;&nbsp;&nbsp;createAccount.setTo("0x1118a901790717520143808672e97d518fef87ea");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Result result = anChainGateWay.createAccount(createAccount, credentials);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Assert.assertNotNull(result.getTxHash());<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Assert.assertNull(result.getError().getCode());<br>
    &nbsp;&nbsp;&nbsp;&nbsp;}<br>
    
 3, query accountInfo<br>
   &nbsp;&nbsp; @Test<br>
    &nbsp;&nbsp;&nbsp;&nbsp;public void queryAccountInfo(){<br>
        &nbsp;&nbsp;&nbsp;&nbsp;AnChainGateWay anChainGateWay = getClient("http://URL:46657/");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;AccountResult result = anChainGateWay.queryAccountInfo("0x1118a901790717520143808672e97d518fef87ea");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Assert.assertNotNull(result.getAccount());<br>
   &nbsp;&nbsp;&nbsp;&nbsp; }<br>
 4, payment<br>
   &nbsp;&nbsp; @Test<br>
   &nbsp;&nbsp;&nbsp;&nbsp; public void testPayment(){<br>
       &nbsp;&nbsp;&nbsp;&nbsp; AnChainGateWay anChainGateWay = getClient("http://URL:46657/");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Payment payment = new Payment();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;payment.setNonce("0");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;payment.setMemo("111");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;payment.setAmount("999");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;payment.setBasefee("100");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;payment.setFrom(credentials.getAddress());<br>
        &nbsp;&nbsp;&nbsp;&nbsp;payment.setTo("0x76f64caecfc23a5923efbb5f6b692b91a70aa429");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Result result = anChainGateWay.payment(payment,credentials);<br>
       &nbsp;&nbsp;&nbsp;&nbsp; Assert.assertNull(result.getError().getCode());<br>
    &nbsp;&nbsp;}<br>
5, manager data<br>
 &nbsp;&nbsp;@Test<br>
   &nbsp;&nbsp; public void testManagerData(){<br>
       &nbsp;&nbsp;&nbsp;&nbsp; AnChainGateWay anChainGateWay = getClient("http://URL:46657/");<br>
       &nbsp;&nbsp;&nbsp;&nbsp; Credentials credentials = &nbsp;&nbsp;Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");<br>
       &nbsp;&nbsp;&nbsp;&nbsp; ManagerData managerData = new ManagerData();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Map<String,Value> keyPair1 = new HashMap<>();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Value value = new Value();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;value.setCategory("E");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;value.setValue("value1");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;keyPair1.put("name3",value);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Value value2 = new Value();<br>
        &nbsp;&nbsp;&nbsp;&nbsp;value2.setCategory("F");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;value2.setValue("value2");<br>
        keyPair1.put("name4",value2);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;managerData.setKeyPairList(keyPair1);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;managerData.setKeyPairList(keyPair1);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;managerData.setBasefee("100");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;managerData.setFrom(credentials.getAddress());<br>
        &nbsp;&nbsp;&nbsp;&nbsp;managerData.setNonce("1");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;managerData.setMemo("memo");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;managerData.setTo("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Result result = anChainGateWay.managerData(managerData,credentials);<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Assert.assertNull(result.getError().getCode());<br>
        &nbsp;&nbsp;&nbsp;&nbsp;Assert.assertNotNull(result.getTxHash());<br>
    &nbsp;&nbsp;}
