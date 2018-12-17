# AnnChain-java-sdk
介绍：AnnChain java sdk库提供了构建交易和连接到底层链Genesis的API

Demo :

 基础请求字段：BaseRequest { basefee,memo,from,to,nonce } 不能为空！
 
1, generate address
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
    
2, create account
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
    
  3, query accountInfo
    @Test
    public void queryAccountInfo(){//
        AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
        AccountResult result = anChainGateWay.queryAccountInfo("0x1118a901790717520143808672e97d518fef87ea");
        Assert.assertNotNull(result.getAccount());
        
    }
 4, payment
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
5, manager data
 @Test
    public void testManagerData(){
        AnChainGateWay anChainGateWay = getClient("http://URL:46657/");
        Credentials credentials = Credentials.create("7cb4880c2d4863f88134fd01a250ef6633cc5e01aeba4c862bedbf883a148ba8");
        ManagerData managerData = new ManagerData();
        Map<String,Value> keyPair1 = new HashMap<>();
        Value value = new Value();
        value.setCategory("E");
        value.setValue("555");
        keyPair1.put("name3",value);
        Value value2 = new Value();
        value2.setCategory("F");
        value2.setValue("666");
        keyPair1.put("name4",value2);
        managerData.setKeyPairList(keyPair1);
        managerData.setKeyPairList(keyPair1);
        managerData.setBasefee("100");
        managerData.setFrom(credentials.getAddress());
        managerData.setNonce("1");
        managerData.setMemo("memo");
        managerData.setTo("0x65188459a1dc65984a0c7d4a397ed3986ed0c853");
        Result result = anChainGateWay.managerData(managerData,credentials);
        Assert.assertNull(result.getError().getCode());
        Assert.assertNotNull(result.getTxHash());
    }
