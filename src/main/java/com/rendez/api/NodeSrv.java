package com.rendez.api;


import com.rendez.api.bean.exception.BaseException;
import com.rendez.api.bean.exception.ErrCode;
import com.rendez.api.bean.model.BlockHashResult;
import com.rendez.api.bean.model.DeployContractResult;
import com.rendez.api.bean.rendez.*;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.Signature;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPList;
import org.ethereum.vm.LogInfo;
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
import java.util.ArrayList;
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
     *
     * 查询交易执行event log
     *
     * @param txHash  交易hash
     * @return
     * @throws IOException
     */
    public List<LogInfo> queryReceipt(String txHash) throws IOException {
        TransactionReceipt receipt = queryReceiptRaw(txHash);
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
    public TransactionReceipt queryReceiptRaw(String txHash) throws IOException {
        Response<BaseResp<ResultQuery>> httpRes = stub.query(QueryType.Receipt.padd(txHash)).execute();
        handleRespQuery(httpRes);
        BaseResp<ResultQuery> resp = httpRes.body();
        log.debug(resp.toString());
        if (resp.getResult().getResult().getData() != null) {
            byte[] rlp = Numeric.hexStringToByteArray(resp.getResult().getResult().getData());
            TransactionReceipt res = new TransactionReceipt(rlp);
            return res;
        } else {
            return null;
        }
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
        return queryContractWithSig(nonce, contractAddress, function, sig);
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
    public List<Type> queryContractWithSig(BigInteger nonce, String contractAddress, Function function, Signature sig) throws IOException {
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        byte[] message = TransactionUtil.encodeWithSig(tx, sig);
        log.debug("raw tx:{}", Numeric.toHexString(message));
        Response<BaseResp<ResultQuery>> httpRes = stub.query(QueryType.Contract.paddByte(message)).execute();
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
    public String deployContractWithSig(String binaryCode, List<Type> constructorParameters, BigInteger nonce, Signature sig, String address,EventCallBack callBack) throws IOException {
        DeployContractResult deployContractResult = doDeployContract(binaryCode, constructorParameters, nonce, sig, address,callBack);
        return deployContractResult.getContractAddr();
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
     public DeployContractResult deployContractCompl(String binaryCode, List<Type> constructorParameters, PrivateKey privateKey, BigInteger nonce,EventCallBack callBack) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        Signature sig = CryptoUtil.generateSignature(tx, privateKey);
        DeployContractResult res = doDeployContract(binaryCode, constructorParameters, nonce, sig, privateKey.getAddress(),callBack);
        return res;
    }

    private DeployContractResult doDeployContract(String binaryCode, List<Type> constructorParameters, BigInteger nonce, Signature sig, String address,EventCallBack callBack) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        byte[] message = TransactionUtil.encodeWithSig(tx, sig);
        String txHash = sendTxToNode(message, false, null);
        DeployContractResult deployContractResult = new DeployContractResult();
        deployContractResult.setTxHash(txHash);
        deployContractResult.setContractAddr(ContractUtils.generateContractAddress(address, nonce));
        if(null != callBack){
            sb.onNext(new QueryRecTask(txHash, callBack, this));
        }
        return deployContractResult;
    }

    /***
     * 通过块hash查询块上有效交易列表
     *
     * @param blockHash
     * @return
     * @throws Exception
     */
    public BlockHashResult blockHashs(String blockHash) throws Exception {
        Response<BaseResp<ResultQuery>> httpRes = stub.query(QueryType.BlockHashs.padd(blockHash)).execute();
        handleRespQuery(httpRes);
        BaseResp<ResultQuery> resp = httpRes.body();
        log.debug(resp.toString());
        if (resp.getResult().getResult().getData() != null) {
            byte[] rlp = Numeric.hexStringToByteArray(resp.getResult().getResult().getData());
            RLPList params = RLP.decode2(rlp);
            RLPList txList = (RLPList) params.get(0);
            if(txList.size()>0){
                BlockHashResult result = new BlockHashResult();
                List<String> txs = new ArrayList<>();
                txList.forEach(item -> txs.add(com.rendez.api.util.ByteUtil.bytesToHex(item.getRLPData())));
                result.setHashs(txs);
                result.setLength(txs.size());
                return result;
            }
        }
        return new BlockHashResult();
    }

    private void handleRespCommit(Response<BaseResp<ResultCommit>> httpRes) {
        if (!httpRes.isSuccessful()) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
        }
        if (StringUtils.isNotBlank(httpRes.body().getError())) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.body().getError());
        }
        if (!httpRes.body().getResult().isSuccess()) {
            throw new BaseException(ErrCode.ERR_NO_DATA_FUND,httpRes.body().getResult().getLog());
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
            throw new BaseException(ErrCode.ERR_NO_DATA_FUND,httpRes.body().getResult().getResult().getLog());
        }
    }
}
