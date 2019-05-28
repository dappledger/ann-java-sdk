package com.rendez.api;


import com.google.protobuf.ByteString;
import com.rendez.api.bean.exception.BaseException;
import com.rendez.api.bean.exception.ErrCode;
import com.rendez.api.bean.model.DeployContractResult;
import com.rendez.api.bean.rendez.*;
import ikhofi.Message;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
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
     * @param credential 秘钥
     * @return
     * @throws IOException
     */
    public List<Type> queryContract(BigInteger nonce, String contractAddress, Function function, Credentials credential) throws IOException {
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        Sign.SignatureData sig = CryptoUtil.generateSignature(tx, credential);
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
    public List<Type> queryContractWithSig(BigInteger nonce, String contractAddress, Function function, Sign.SignatureData sig) throws IOException {
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
     * @param credential
     * @return 交易hash
     * @throws IOException
     */
    public String callContractEvm(BigInteger nonce, String contractAddress, Function function, Credentials credential) throws IOException {
        return callContractEvm(nonce, contractAddress, function, credential, null);
    }


    public String callContractEvm(BigInteger nonce, String contractAddress, Function function, Credentials credential, EventCallBack callBack) throws IOException {
        // default call sync
        return callContractEvm(nonce, contractAddress, function, credential, callBack, true);
    }

    /**
     *
     * 调用evm 合约
     *
     * @param nonce nonce
     * @param contractAddress 合约地址
     * @param function 函数定义
     * @param credential 秘钥
     * @param callBack 回调函数
     * @param isSyncCall 是否同步调用
     * @return txHash 交易哈希
     * @throws IOException
     */
    public String callContractEvm(BigInteger nonce, String contractAddress, Function function, Credentials credential, EventCallBack callBack, boolean isSyncCall) throws IOException {
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, contractAddress, function);
        Sign.SignatureData sig = CryptoUtil.generateSignature(tx, credential);
        return callContractEvmWithSig(nonce, contractAddress, function, sig, callBack, isSyncCall);
    }


    public String callContractEvmWithSig(BigInteger nonce, String contractAddress, Function function, Sign.SignatureData sig, EventCallBack callBack) throws IOException {
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
    public String callContractEvmWithSig(BigInteger nonce, String contractAddress, Function function, Sign.SignatureData sig, EventCallBack callBack, boolean isSyncCall) throws IOException {
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
     * 交易批量上链, 最大500笔
     * @param txs
     * @return
     * @throws IOException
     */
    public List<String> batchCallContractEvm(List<byte[]> txs) throws IOException {
        if (txs.size() > RendezUtil.MAX_BATCH_SIZE){
            throw new BaseException("max allow batch size: " + RendezUtil.MAX_BATCH_SIZE);
        }
        List<String> txHashes = new LinkedList<>();
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(RendezUtil.BATCH_TAG_PREFIX);
        output.write(RendezUtil.ToInt32Bytes(txs.size()));
        for (byte[] tx: txs) {
            RendezUtil.writeToStream(output, tx);
        }
        String txhashConcat = sendTxToNode(output.toByteArray(), true, null);
        // 服务端返回的交易哈希是每单笔交易哈希的串联
        int avgTxhashLength = txhashConcat.length()/txs.size();
        for (int i=0;i<txhashConcat.length();i+=avgTxhashLength){
            // 从txhashConcat生成每单笔交易哈希
            String txHash = txhashConcat.substring(i, i+avgTxhashLength);
            txHashes.add("0x" + txHash);
        }
        return txHashes;
    }

    /**
     * 调用jvm 合约
     *
     * @param nonce
     * @param contractAddress
     * @param
     * @param callBack
     * @return txHash
     * @throws IOException
     */
    public String callContractJvm(BigInteger nonce, String contractAddress, String method, List<String> args, Credentials credential, EventCallBack callBack) throws IOException {
        Message.TransactionPbTmp.Builder buider = Message.TransactionPbTmp.newBuilder();
        buider.setFrom(ByteString.copyFromUtf8(credential.getAddress()));
        buider.setTo(contractAddress);
        buider.setMethod(method);
        buider.setNonce(nonce.longValue());
        if (args != null) {
            for (int i = 0; i < args.size(); i++) {
                buider.setArgs(i, args.get(i));
            }
        }

        byte[] bs = buider.build().toByteArray();
        byte[] hash = Hash.sha3(bs);
        Sign.SignatureData sign = Sign.signMessage(bs, credential.getEcKeyPair());

        Message.TransactionPb.Builder pbBuilder = Message.TransactionPb.newBuilder();
        pbBuilder.setFrom(ByteString.copyFromUtf8(credential.getAddress()));
        pbBuilder.setTo(contractAddress);
        pbBuilder.setMethod(method);
        pbBuilder.setNonce(nonce.longValue());
        if (args != null) {
            for (int i = 0; i < args.size(); i++) {
                pbBuilder.setArgs(i, args.get(i));
            }
        }
        pbBuilder.setHash(ByteString.copyFrom(hash));
        pbBuilder.setS(ByteString.copyFrom(sign.getS()));
        pbBuilder.setR(ByteString.copyFrom(sign.getR()));
        pbBuilder.setV(ByteString.copyFrom(new byte[]{sign.getV()}));

        String hashStr = Numeric.toHexString(hash);

        Response<BaseResp<ResultCommit>> httpRes = stub.broadcastTxCommit(Numeric.toHexString(pbBuilder.build().toByteArray())).execute();
        handleRespCommit(httpRes);
        sb.onNext(new QueryRecTask(hashStr, callBack, this));

        return hashStr;
    }

    /**
     *
     * 使用私钥部署合约
     *
     * @param binaryCode 合约二进制编译结果
     * @param
     * @param credentials
     * @param nonce
     * @return contract address 合约地址
     * @throws IOException
     */
    public String deployContract(String binaryCode, List<Type> constructorParameters, Credentials credentials, BigInteger nonce) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        Sign.SignatureData sig = CryptoUtil.generateSignature(tx, credentials);
        DeployContractResult res = doDeployContract(binaryCode, constructorParameters, nonce, sig, credentials.getAddress());
        return res.getContractAddr();
    }

    public DeployContractResult deployContractCompl(String binaryCode, List<Type> constructorParameters, Credentials credentials, BigInteger nonce) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        Sign.SignatureData sig = CryptoUtil.generateSignature(tx, credentials);
        DeployContractResult res = doDeployContract(binaryCode, constructorParameters, nonce, sig, credentials.getAddress());
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
    public String deployContractWithSig(String binaryCode, List<Type> constructorParameters, BigInteger nonce, Sign.SignatureData sig, String address) throws IOException {
        DeployContractResult deployContractResult = doDeployContract(binaryCode, constructorParameters, nonce, sig, address);
        return deployContractResult.getContractAddr();
    }

    private DeployContractResult doDeployContract(String binaryCode, List<Type> constructorParameters, BigInteger nonce, Sign.SignatureData sig, String address) throws IOException {
        RawTransaction tx = TransactionUtil.createDelopyContractTransaction(binaryCode, constructorParameters, nonce);
        byte[] message = TransactionUtil.encodeWithSig(tx, sig);
        String txHash = sendTxToNode(message, true, null);
        DeployContractResult deployContractResult = new DeployContractResult();
        deployContractResult.setTxHash(txHash);
        deployContractResult.setContractAddr(ContractUtils.generateContractAddress(address, nonce));
        return deployContractResult;
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
}
