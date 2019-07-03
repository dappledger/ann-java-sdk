package com.rendez.api;


import com.rendez.api.bean.exception.BaseException;
import com.rendez.api.bean.exception.ErrCode;
import com.rendez.api.bean.model.BlockDbResult;
import com.rendez.api.bean.model.BlockHashResult;
import com.rendez.api.bean.model.BlockdbQueryRequest;
import com.rendez.api.bean.rendez.*;
import com.rendez.api.bean.request.BaseRequest;
import com.rendez.api.blockdb.BlockDbTransaction;
import com.rendez.api.blockdb.BlockDbUtils;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.PrivateKeyECDSA;
import com.rendez.api.crypto.Signature;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang.StringUtils;
import org.web3j.utils.Numeric;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;


@Data
@Slf4j
public class NodeSrv {
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

    }

    /***
     * blockdb存数据，默认使用异步方式，因为共识时间长，同步提交等待时间过长
     * @param privateKey
     * @param value
     * @param isAsyn true:异步，false:同步
     * @return txHash
     * @throws IOException
     */
    public String blockdbPuter(String privateKey, byte[] value, boolean isAsyn) throws IOException {
        PrivateKey prk = new PrivateKeyECDSA(privateKey);
        //assemble transaction entity
        BlockDbTransaction transaction = new BlockDbTransaction(prk.getAddress(), BigInteger.valueOf(System.currentTimeMillis()), value);
        //signature transaction
        Signature signature = CryptoUtil.generateBlockSignature(transaction, prk);
        //encode tx with signature
        byte[] message = TransactionUtil.encodeWithSig(transaction, signature);
        //submit to block chain
        BaseResult result = sendTxToNode(message, isAsyn);
        log.info("block return value : {}",result);
        //
        return CryptoUtil.txHash(transaction);
    }

    /***
     * blockdb查询
     * @param txHash
     * @return
     * @throws IOException
     */
    public BlockDbResult blockdbGeter(String txHash) throws IOException {
        if(StringUtils.isEmpty(txHash)){
            log.error("input txHash is empty!");
            return new BlockDbResult();
        }
        txHash = txHash.startsWith("0x")?txHash.substring(2):txHash;
        BlockdbQueryRequest bqr = new BlockdbQueryRequest("GET",txHash);
        BaseRequest request = new BaseRequest("abci_query", bqr);
        Response<BaseResp<ResultABCIQuery>> httpRes = stub.abciquery(request).execute();
        handleRespQuery(httpRes);
        BaseResp<ResultABCIQuery> resp = httpRes.body();

        BlockDbResult db = BlockDbUtils.blockDbResultBuilder(resp.getResult().getResponse().getValue());

        return db;
    }

    /***
     * 通过[块hash]查询块上有效交易列表
     *
     * @param hash
     * @return
     * @throws Exception
     */
    public BlockHashResult blockHashs(String hash) throws Exception {
        if(StringUtils.isEmpty(hash)){
            log.error("input hash is empty!");
            return new BlockHashResult();
        }
        hash = hash.startsWith("0x")?hash.substring(2):hash;
        BlockdbQueryRequest bqr = new BlockdbQueryRequest("BLOCK",hash);
        BaseRequest request = new BaseRequest("abci_query", bqr);
        Response<BaseResp<ResultABCIQuery>> httpRes = stub.abciquery(request).execute();
        handleRespQuery(httpRes);
        BaseResp<ResultABCIQuery> resp = httpRes.body();
        if (resp.getResult() != null && resp.getResult().getResponse() != null && resp.getResult().getResponse().getValue() != null) {
            BlockHashResult result = BlockDbUtils.blockHashResultBuilder(resp.getResult().getResponse().getValue());
            return result;
        }
        return new BlockHashResult();
    }

    /**
     * 将序列化好的交易广播到链节点
     * broadcast signed and serialized tx to node rpc
     *
     * @param txMessage  交易（签名并序列化完成）
     * @param isAsyn 是否异步上链
     * @return
     * @throws IOException
     */
    public BaseResult sendTxToNode(byte[] txMessage, boolean isAsyn) throws IOException {
        Response<BaseResp<BaseResult>> httpRes;
        if (!isAsyn) {
            httpRes = stub.broadcastTxCommit(Numeric.toHexString(txMessage)).execute();
        } else {
            httpRes = stub.broadcastTxAsync(Numeric.toHexString(txMessage)).execute();
        }

        handleRespCommit(httpRes);
        return httpRes.body().getResult();
    }

    private void handleRespCommit(Response<BaseResp<BaseResult>> httpRes) {
        if (!httpRes.isSuccessful()) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
        }
        if (!httpRes.body().getResult().isSuccess()) {
            throw new BaseException(httpRes.body().getResult().getLog());
        }
    }

    private void handleRespQuery(Response<BaseResp<ResultABCIQuery>> httpRes) {
        if (!httpRes.isSuccessful()) {
            throw new BaseException(ErrCode.ERR_NODEAPI, httpRes.raw().toString());
        }
        if (httpRes.body().getResult() == null || httpRes.body().getResult().getResponse() ==null || httpRes.body().getResult().getResponse().getCode() != 0) {
            throw new BaseException("blockdb not fund!");
        }
    }
}
