package com.genesis.api;

import com.genesis.api.bean.exception.BaseException;
import com.genesis.api.bean.exception.ErrCode;
import com.genesis.api.bean.model.DeployContractResult;
import com.genesis.api.bean.model.QueryBlockTransactionHashs;
import com.genesis.api.bean.model.QueryKVResult;
import com.genesis.api.bean.model.QueryPrefixKVs;
import com.genesis.api.bean.model.QueryTransaction;
import com.genesis.api.bean.model.QueryTransactionPayload;
import com.genesis.api.bean.model.QueryTransactionReceipt;
import com.genesis.api.bean.model.RawTransactionData;
import com.genesis.api.bean.model.SendTransactionResult;
import com.genesis.api.bean.model.TransactionKVTx;
import com.genesis.api.bean.genesis.*;
import com.genesis.api.bean.genesis.ResultBlock.TxData;
import com.genesis.api.crypto.PrivateKey;
import com.genesis.api.crypto.Signature;
import com.genesis.api.crypto.SignatureECDSA;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import org.ethereum.util.RLP;
import org.ethereum.util.RLP.LList;
import org.ethereum.vm.LogInfo;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.*;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Data
@Slf4j
public class NodeSrv {
    private Subject<QueryRecTask> sb;
    private NodeApi stub;
    
    public NodeSrv(String url) {
        OkHttpClient okClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        stub = retrofit.create(NodeApi.class);

        sb = PublishSubject.create();

        sb.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new LogObserver());
    }

    /**
     * 查询nonce
     *
     * @param address 账户地址
     * @return nonce
     * @throws IOException
     */
    public Integer queryNonce(String address) throws IOException {
        Response<BaseResp<ResultQuery>> httpResp = stub.query(QueryType.Nonce.padd(address)).execute();
        handleRespQuery(httpResp);
        String nonceStr = httpResp.body().getResult().getResult().getData();
        if (StringUtils.isBlank(nonceStr)) {
            throw new BaseException("can not obtain nonce for address " + address);
        }
        byte[] rlpEncoded = Numeric.hexStringToByteArray(nonceStr);
        RlpList decode = RlpDecoder.decode(rlpEncoded);
        String hexValue = ((RlpString) decode.getValues().get(0)).asString();
        String cleanHexPrefix = Numeric.cleanHexPrefix(hexValue);
        return StringUtils.isBlank(cleanHexPrefix) ? 0 : Integer.parseInt(cleanHexPrefix, 16);
    }
    
    /**
     * 根据hash查询tx
     *
     * @param hash 交易hash
     * @return tx
     * @throws IOException
     */
    public QueryTransaction queryTxByHash(String hashByte) throws IOException {
        Response<BaseResp<ResultQuery>> httpResp = stub.transaction(hashByte).execute();
        handleRespQuery(httpResp);
        BaseResp<ResultQuery> resp = httpResp.body();
        log.debug(resp.toString());
        if (resp.getResult().getResult().getData() != null) {
        	byte[] rlp = Numeric.hexStringToByteArray(resp.getResult().getResult().getData());
        	QueryTransaction res = new QueryTransaction(rlp);
            return res;
        } else {
            return null;
        }
    }
    
    public RawTransactionData queryRawTransactionByHash(String hashByte) throws IOException {
    	Response<BaseResp<ResultQuery>> httpResp = stub.transaction(hashByte).execute();
        handleRespQuery(httpResp);
        BaseResp<ResultQuery> resp = httpResp.body();
        log.debug(resp.toString());
        if (resp.getResult().getResult().getData() != null) {
        	byte[] rlp = Numeric.hexStringToByteArray(resp.getResult().getResult().getData());
        	QueryTransaction res = new QueryTransaction(rlp);
            RawTransactionData txdata = new RawTransactionData(res.getRawTransaction());         
            return txdata;
        } else {
            return null;
        }
    }
    
    /**
     * 根据区块高度查询txhashs
     *
     * @param height 区块高度
     * @return TransactionHashs
     * @throws IOException
     */
    public QueryBlockTransactionHashs queryTransactionHashsByHeight(BigInteger height) throws IOException{
    	Response<BaseResp<ResultBlock>> httpResp = stub.block(height).execute();
    	handleRespBlock(httpResp);
        BaseResp<ResultBlock> resp = httpResp.body();
        if (resp.getResult().getBlock() != null) {
        	long total = resp.getResult().getBlock().getHeader().getNum_txs();
        	TxData blockdata = resp.getResult().getBlock().getData();
        	if (total != blockdata.getExtxs().length + blockdata.getTxs().length) {
        		return null;
        	}
        	      	
        	String[] txs = blockdata.getTxs();
        	String[] extxs = blockdata.getTxs();
        	int base = txs.length;
        	int ex = extxs.length;
        	String[] hashs = new String[base+ex];
        	for (int i = 0; i <txs.length; i++) {
        		byte[] txByte = Numeric.hexStringToByteArray(txs[i]);
        		byte[] txhash = Hash.sha3(txByte);
        		hashs[i] = Numeric.toHexString(txhash);
        	}
        	
        	for (int i = 0; i <ex; i++) {
        		hashs[base+i] = Numeric.toHexString(Hash.sha3(Numeric.hexStringToByteArray(extxs[i])));
        	}
        	log.info("hashs:"+hashs);
        	QueryBlockTransactionHashs txHashs = new QueryBlockTransactionHashs(hashs,total);
            return txHashs;
        } else {
            return null;
        }
    }
    
    /**
     *
     * 查询交易执行event log
     *
     * @param txHash  交易hash
     * @return
     * @throws IOException
     */
    public List<LogInfo> queryReceipt(String txHash) throws IOException {
    	QueryTransactionReceipt receipt = queryReceiptRaw(txHash);
        if (receipt != null) {
            return receipt.getLogInfoList();
        } else {
            return null;
        }
    }

    /**
     *
     * 查询交易执行receipt
     *
     * @param txHash  交易hash
     * @return
     * @throws IOException
     */
    public QueryTransactionReceipt queryReceiptRaw(String txHash) throws IOException {
        Response<BaseResp<ResultQuery>> httpRes = stub.query(QueryType.Receipt.padd(txHash)).execute();
        handleRespQuery(httpRes);
        BaseResp<ResultQuery> resp = httpRes.body();
        log.debug(resp.toString());
        QueryTransactionReceipt res = new QueryTransactionReceipt();
        if (resp.getResult().getResult().getData() != null) {
            byte[] rlp = Numeric.hexStringToByteArray(resp.getResult().getResult().getData());
            res = new QueryTransactionReceipt(rlp);
        } else {
            return null;
        }
        
        QueryTransaction tx = queryTxByHash(txHash);
        if (tx == null) {
        	return null;
        }
        
        RawTransactionData txdata = new RawTransactionData(tx.getRawTransaction());
        
        String fromAddr = new String();
        try {
        	fromAddr = getFromAddress(txdata);
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        if (fromAddr.indexOf("0x") != -1){
        	fromAddr = fromAddr.substring(2);
        }
            
        res.setBlockHash(tx.getBlockHash());
        res.setBlockHeight(tx.getBlockHeight());
        res.setTransactionIndex(tx.getTransactionIndex());
        res.setFrom(Hex.decode(fromAddr));
        res.setTimestamp(tx.getTimestamp());
        res.setTo(txdata.getTo());
        
        return res;
    }
    
    private String getFromAddress(RawTransactionData txdata) throws SignatureException{
        RawTransaction rtx = RawTransaction.createTransaction(txdata.getNonce(), txdata.getGasprice(), txdata.getGas(), Hex.toHexString(txdata.getTo()), txdata.getValue(), Hex.toHexString(txdata.getInput()));
        Signature sig = new SignatureECDSA(txdata.getV().byteValue(), txdata.getR().toByteArray(), txdata.getS().toByteArray());
        String txMsg = Numeric.toHexString(TransactionUtil.encodeWithSig(rtx, sig));
        SignedRawTransaction signedRawTransaction = (SignedRawTransaction) TransactionDecoder.decode(txMsg);
        String signerAddress = signedRawTransaction.getFrom();
        return signerAddress;
    }


    /**
     * 用秘钥查询合约
     * @param nonce
     * @param contractAddress 合约地址
     * @param function  合约方法
     * @param privateKey 秘钥
     * @return
     * @throws IOException
     */
    public List<Type> queryContract(BigInteger nonce, String contractAddress, Function function, PrivateKey privateKey) throws IOException{
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        Signature sig = CryptoUtil.generateSignature(tx, privateKey);
        return queryContractWithSig(nonce, contractAddress, function, BigInteger.valueOf(0), sig);
    }

    /**
     * 签名查询合约
     * @param nonce
     * @param contractAddress 合约地址
     * @param function  合约方法
     * @param sig 签名
     * @return
     * @throws IOException
     */
    public List<Type> queryContractWithSig(BigInteger nonce, String contractAddress, Function function, BigInteger height, Signature sig) throws IOException {
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        byte[] message = TransactionUtil.encodeWithSig(tx, sig);

    	byte[] h = Numeric.toBytesPadded(height,8);

    	byte[] par = new byte[message.length+h.length];
    	System.arraycopy(message, 0, par, 0, message.length);  
        System.arraycopy(h, 0, par, message.length, h.length); 
        
        
    
        Response<BaseResp<ResultQuery>> httpRes = stub.query(QueryType.ContractByHeight.paddByte(par)).execute();
        handleRespQuery(httpRes);
        BaseResp<ResultQuery> resp = httpRes.body();
        if (resp.getResult().getResult().getData() != null) {
            String respData =  resp.getResult().getResult().getData();
            byte[] rlpEncoded = Numeric.hexStringToByteArray(respData);
            List<Type> someTypes = FunctionReturnDecoder.decode(
                    Numeric.toHexString(rlpEncoded), function.getOutputParameters());
            return someTypes;
        } else {
            return null;
        }
    }
    
    /**
    *
    * 根据高度查询 合约
    *
    * @param nonce
    * @param contractAddress  合约地址
    * @param function 合约函数定义
    * @param privateKey
    * @return 交易hash
    * @throws IOException
    */
    public List<Type> queryContractByHeight(BigInteger nonce, String contractAddress, Function function, PrivateKey privateKey, BigInteger height) throws IOException{
    	RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        Signature sig = CryptoUtil.generateSignature(tx, privateKey);
        return queryContractWithSig(nonce, contractAddress, function, height, sig);
    } 

     
    /**
     *
     * 调用evm 合约
     *
     * @param nonce
     * @param contractAddress  合约地址
     * @param function 合约函数定义
     * @param privateKey
     * @return 交易hash
     * @throws IOException
     */
    public String callContractEvm(BigInteger nonce, String contractAddress, Function function, PrivateKey privateKey) throws IOException {
        return callContractEvm(nonce, contractAddress, function, privateKey, null);
    }


    public String callContractEvm(BigInteger nonce, String contractAddress, Function function, PrivateKey privateKey, EventCallBack callBack) throws IOException {
        // default call sync
        return callContractEvm(nonce, contractAddress, function, privateKey, callBack, true);
    }

    /**
     *
     * 调用evm 合约
     *
     * @param nonce nonce
     * @param contractAddress 合约地址
     * @param function 函数定义
     * @param privateKey 秘钥
     * @param callBack 回调函数
     * @param isSyncCall 是否同步调用
     * @return txHash 交易哈希
     * @throws IOException
     */
    public String callContractEvm(BigInteger nonce, String contractAddress, Function function, PrivateKey privateKey, EventCallBack callBack, boolean isSyncCall) throws IOException{
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        Signature sig = CryptoUtil.generateSignature(tx, privateKey);
        return callContractEvmWithSig(nonce, contractAddress, function, sig, callBack, isSyncCall);
    }


    public String callContractEvmWithSig(BigInteger nonce, String contractAddress, Function function, Signature sig, EventCallBack callBack) throws IOException {
        // default call sync
        return callContractEvmWithSig(nonce, contractAddress, function, sig, callBack, true);
    }

    /**
     *
     * 使用生成好的签名调用evm 合约
     *
     * @param nonce nonce
     * @param contractAddress 合约地址
     * @param function 函数定义
     * @param sig 交易签名
     * @param callBack 回调函数
     * @param isSyncCall 是否同步调用
     * @return txHash 交易哈希
     * @throws IOException
     */
    public String callContractEvmWithSig(BigInteger nonce, String contractAddress, Function function, Signature sig, EventCallBack callBack, boolean isSyncCall) throws IOException {
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        byte[] message = TransactionUtil.encodeWithSig(tx, sig);
        String txHash = sendTxToNode(message, isSyncCall, callBack);
        return txHash;
    }


    /**
     * 将序列化好的交易广播到链节点
     * broadcast signed and serialized tx to node rpc
     * @param txMessage 交易（签名并序列化完成）
     * @param isSyncCall 同步/异步上链
     * @param callBack 回调
     * @return
     * @throws IOException
     */
    public String sendTxToNode(byte[] txMessage, boolean isSyncCall, EventCallBack callBack) throws IOException {
        Response<BaseResp<ResultCommit>> httpRes;
        if (isSyncCall){
            httpRes = stub.broadcastTxCommit(Numeric.toHexString(txMessage)).execute();
        }else {
            httpRes = stub.broadcastTxAsync(Numeric.toHexString(txMessage)).execute();
        }
        handleRespCommit(httpRes);
        String txHash = httpRes.body().getResult().getTx_hash();
        if (callBack != null) {
            sb.onNext(new QueryRecTask(txHash, callBack, this));
        }
        return txHash;
    }
    
    /**
     	* 将序列化好的KV交易广播到链节点
     * broadcast signed and serialized tx to node rpc
     * @param txMessage 交易（签名并序列化完成）
     * @param isSyncCall 同步/异步上链
     * @param callBack 回调
     * @return
     * @throws IOException
     */
    public SendTransactionResult sendTxToNodeWithError(byte[] txMessage, boolean isSyncCall, EventCallBack callBack) throws IOException {
        Response<BaseResp<ResultCommit>> httpRes;
        if (isSyncCall){
            httpRes = stub.broadcastTxCommit(Numeric.toHexString(txMessage)).execute();
        }else {
            httpRes = stub.broadcastTxAsync(Numeric.toHexString(txMessage)).execute();
        }
        handleRespCommitError(httpRes);
        
        SendTransactionResult res = new SendTransactionResult();
        if (StringUtils.isNotBlank(httpRes.body().getError())) {
        	res.setErrMsg(httpRes.body().getError());
        }else {
        	res.setTxHash(httpRes.body().getResult().getTx_hash());
        }
        return res;
    }

    /**
     *
     * 使用私钥部署合约
     *
     * @param binaryCode 合约二进制编译结果
     * @param
     * @param privateKey
     * @param nonce
     * @return contract address 合约地址
     * @throws IOException
     */
    public String deployContract(String binaryCode, List<Type> constructorParameters, PrivateKey privateKey, BigInteger nonce) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        Signature sig = CryptoUtil.generateSignature(tx, privateKey);
        DeployContractResult res = doDeployContract(binaryCode, constructorParameters, nonce, sig, privateKey.getAddress());
        return res.getContractAddr();
    }

    public DeployContractResult deployContractCompl(String binaryCode, List<Type> constructorParameters, PrivateKey privateKey, BigInteger nonce) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        Signature sig = CryptoUtil.generateSignature(tx, privateKey);
        DeployContractResult res = doDeployContract(binaryCode, constructorParameters, nonce, sig, privateKey.getAddress());
        return res;
    }


    /**
     *
     * 使用签名部署合约
     *
     * @param binaryCode 合约二进制编译结果
     * @param
     * @param address 用户账户地址
     * @param nonce
     * @return contract address 合约地址
     * @throws IOException
     */
    public String deployContractWithSig(String binaryCode, List<Type> constructorParameters, BigInteger nonce, Signature sig, String address) throws IOException {
        DeployContractResult deployContractResult = doDeployContract(binaryCode, constructorParameters, nonce, sig, address);
        return deployContractResult.getContractAddr();
    }

    private DeployContractResult doDeployContract(String binaryCode, List<Type> constructorParameters, BigInteger nonce, Signature sig, String address) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        byte[] message = TransactionUtil.encodeWithSig(tx, sig);
        String txHash = sendTxToNode(message, true, null);
        DeployContractResult deployContractResult = new DeployContractResult();
        deployContractResult.setTxHash(txHash);
        deployContractResult.setContractAddr(ContractUtils.generateContractAddress(address, nonce));
        return deployContractResult;
    }
    
    
    /**
    *
    * 调用kv 交易
    *
    * @param nonce
    * @param key  合约地址
    * @param value 合约函数定义
    * @param privateKey
    * @return 交易hash
    * @throws IOException
    */
   public SendTransactionResult putKv(BigInteger nonce, String key, String value, PrivateKey privateKey) throws IOException {
	   RawTransaction tx = TransactionUtil.createPutKVTransaction(nonce,key,value);
	   Signature sig = CryptoUtil.generateSignature(tx, privateKey);
	   SendTransactionResult res = callKvTxWithSig(nonce, key, value, sig, true);
       return res;
   }

   public SendTransactionResult putKvAsync(BigInteger nonce,String key, String value, PrivateKey privateKey) throws IOException {
	   RawTransaction tx = TransactionUtil.createPutKVTransaction(nonce,key,value);
	   Signature sig = CryptoUtil.generateSignature(tx, privateKey);
	   SendTransactionResult res = callKvTxWithSig(nonce, key, value, sig, false);
       return res;
   }
   
   public SendTransactionResult putKvSignature(BigInteger nonce,String key, String value, Signature sig) throws IOException {
	   SendTransactionResult res = callKvTxWithSig(nonce, key, value, sig, true);
       return res;
   }
   
   public SendTransactionResult putKvSignatureAsync(BigInteger nonce,String key, String value, Signature sig) throws IOException {
	   SendTransactionResult res = callKvTxWithSig(nonce, key, value, sig, false);
       return res;
   }
   
   /**
   *
   * 使用生成好的签名调用kv 交易
   *
   * @param nonce nonce
   * @param key key
   * @param value value
   * @param sig 交易签名
   * @param isSyncCall 是否同步调用
   * @return txHash 交易哈希
   * @throws IOException
   */
   public SendTransactionResult callKvTxWithSig(BigInteger nonce, String key, String value, Signature sig, boolean isSyncCall) throws IOException {
	   RawTransaction tx = TransactionUtil.createPutKVTransaction(nonce,key,value);
       byte[] message = TransactionUtil.encodeWithSig(tx, sig);
       SendTransactionResult res = sendTxToNodeWithError(message, isSyncCall, null);
       return res;
   }
   
   /**
   *
   *查詢kv 交易
   *
   * @return value
   * @throws IOException
   */
   public QueryKVResult getKvValueWithKey(String key) throws IOException {
	   Response<BaseResp<ResultQuery>> httpResp = stub.query(QueryType.Key.paddString(key)).execute();
       handleRespKVQuery(httpResp);
       BaseResp<ResultQuery> resp = httpResp.body();
       log.debug(resp.toString());
       QueryKVResult res = new QueryKVResult();
       if (resp.getResult().getResult().isSuccess()) {
    	   String value = resp.getResult().getResult().getData();
           res.setValue(value);
       }else {
    	   res.setErrMsg(resp.getResult().getResult().getLog());
       }
       return res;
   }
   
   public String hexToASCII(String hexValue)
   {
       StringBuilder output = new StringBuilder("");
       for (int i = 0; i < hexValue.length(); i += 2)
       {
           String str = hexValue.substring(i, i + 2);
           output.append((char) Integer.parseInt(str, 16));
       }
       return output.toString();
   }
   
   /**
   *
   * 批量查詢kv 交易
   * @param prefix 前缀
   * @param lastkey 起始前一个key
   * @param limit 返回數量
   * @return value
   * @throws IOException
   */
   public QueryPrefixKVs getKvValueWithPrefix(String prefix,String lastkey,BigInteger limit) throws IOException {
	   byte[] quryData = TransactionUtil.encodeKVPrefixQuery(prefix,lastkey,limit);
	   Response<BaseResp<ResultQuery>> httpResp = stub.query(QueryType.KeyPrefix.paddByte(quryData)).execute();
	   handleRespKVQuery(httpResp);
       BaseResp<ResultQuery> resp = httpResp.body();
       log.debug(resp.toString());
       
       QueryPrefixKVs res = new QueryPrefixKVs();
       if (resp.getResult().getResult().isSuccess()) {
    	   String respData = resp.getResult().getResult().getData();
    	   byte[] rlpEncoded = Numeric.hexStringToByteArray(respData);
    	   LList list = RLP.decodeLazyList(rlpEncoded);
    	   TransactionKVTx[] kvs = new TransactionKVTx[list.size()];
    	   for (int i = 0; i <list.size(); i++) {
    		   TransactionKVTx  kv = new TransactionKVTx(list.getBytes(i));
    		   kvs[i] = kv;
    	   }
    	   res.setTxs(kvs);
       }else {
    	   res.setErrMsg(resp.getResult().getResult().getLog()); 
       }
       return res;
   }
   
   
   private void handleRespCommitError(Response<BaseResp<ResultCommit>> httpRes) {
       if (!httpRes.isSuccessful()) {
           throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
       }
   }

    private void handleRespCommit(Response<BaseResp<ResultCommit>> httpRes) {
        if (!httpRes.isSuccessful()) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
        }
        if (StringUtils.isNotBlank(httpRes.body().getError())) {
        	throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.body().getError());
        }
        if (!httpRes.body().getResult().isSuccess()) {
            throw new BaseException(httpRes.body().getResult().getLog());
        }
    }


    private void handleRespQuery(Response<BaseResp<ResultQuery>> httpRes) {
        if (!httpRes.isSuccessful()) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
        }
        if (StringUtils.isNotBlank(httpRes.body().getError())) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.body().getError());
        }
        if (!httpRes.body().getResult().getResult().isSuccess()) {
            throw new BaseException(httpRes.body().getResult().getResult().getLog());
        }
    }
    
    private void handleRespKVQuery(Response<BaseResp<ResultQuery>> httpRes) {
        if (!httpRes.isSuccessful()) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
        }
    }
    
    private void handleRespBlock(Response<BaseResp<ResultBlock>> httpRes) {
        if (!httpRes.isSuccessful()) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
        }
        if (StringUtils.isNotBlank(httpRes.body().getError())) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.body().getError());
        }
    }
    
    
    /**
    *
    * 调用payload交易
    *
    * @param nonce
    * @param key  合约地址
    * @param value 合约函数定义
    * @param privateKey
    * @return 交易hash
    * @throws IOException
    */
   public SendTransactionResult sendPayloadTx(BigInteger nonce,String to, String payload, BigInteger value, PrivateKey privateKey) throws IOException {
	   RawTransaction tx = TransactionUtil.createPayloadTransaction(nonce,to,payload,value);
	   Signature sig = CryptoUtil.generateSignature(tx, privateKey);
	   SendTransactionResult res = callPayloadWithSig(nonce,to, payload, value, sig, true);
       return res;
   }

   public SendTransactionResult sendPayloadTxAsync(BigInteger nonce,String to,String payload, BigInteger value, PrivateKey privateKey) throws IOException {
	   RawTransaction tx = TransactionUtil.createPayloadTransaction(nonce,to,payload,value);
	   Signature sig = CryptoUtil.generateSignature(tx, privateKey);
	   SendTransactionResult res = callPayloadWithSig(nonce,to, payload, value, sig, false);
       return res;
   }
   
   public SendTransactionResult callPayloadWithSig(BigInteger nonce,String to,String payload, BigInteger value, Signature sig, boolean isSyncCall) throws IOException {
       RawTransaction tx = TransactionUtil.createPayloadTransaction(nonce,to, payload, value);
       byte[] message = TransactionUtil.encodeWithSig(tx, sig);
       SendTransactionResult res = sendTxToNodeWithError(message, isSyncCall, null);
       return res;
   }
   
   /**
   *
   * 查詢payload 交易
   * @param txhash
   * @return 
   * @throws IOException
   */
   public QueryTransactionPayload getPayloadWithHash(String hash) throws IOException {
	   Response<BaseResp<ResultQuery>> httpResp = stub.query(QueryType.Payload.ppadd(hash)).execute();
       handleRespKVQuery(httpResp);
       BaseResp<ResultQuery> resp = httpResp.body();
       log.debug(resp.toString());
       QueryTransactionPayload res = new QueryTransactionPayload();
       if (resp.getResult().getResult().isSuccess()) {
    	   String value = resp.getResult().getResult().getData();
           res.setPayload(value);
       }else {
    	   res.setErrMsg(resp.getResult().getResult().getLog());
       }
       return res;
   }
}
