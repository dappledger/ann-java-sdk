package com.rendez.api;


import com.rendez.api.bean.exception.BaseException;
import com.rendez.api.bean.exception.ErrCode;
import com.rendez.api.bean.model.BlockDbResult;
import com.rendez.api.bean.rendez.BaseResp;
import com.rendez.api.bean.rendez.ResultCommit;
import com.rendez.api.bean.rendez.ResultQuery;
import com.rendez.api.blockdb.BlockDbTransaction;
import com.rendez.api.blockdb.BlockDbUtils;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.PrivateKeyECDSA;
import com.rendez.api.crypto.Signature;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
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

    public String blockdbPuter(String privateKey, byte[] value, boolean isSyn) throws IOException {
        PrivateKey prk = new PrivateKeyECDSA(privateKey);
        BlockDbTransaction transaction = new BlockDbTransaction(prk.getAddress(), BigInteger.valueOf(System.currentTimeMillis()), value);
        Signature signature = CryptoUtil.generateBlockSignature(transaction, prk);
        byte[] message = TransactionUtil.encodeWithSig(transaction, signature);
        String txHash = sendTxToNode(message, isSyn);
        return txHash;
    }

    public BlockDbResult blockdbGeter(String key) throws IOException {

        Response<BaseResp<ResultQuery>> httpRes = stub.query(Numeric.toHexString(Numeric.hexStringToByteArray(key))).execute();
        handleRespQuery(httpRes);
        BaseResp<ResultQuery> resp = httpRes.body();
        log.debug(resp.toString());
        if (resp.getResult().getResult().getData() != null) {
            return BlockDbUtils.blockDbResultBuilder(resp.getResult().getResult().getData());
        }

        return new BlockDbResult();
    }

    /**
     * 将序列化好的交易广播到链节点
     * broadcast signed and serialized tx to node rpc
     *
     * @param txMessage  交易（签名并序列化完成）
     * @param isSyncCall 同步/异步上链
     * @return
     * @throws IOException
     */
    public String sendTxToNode(byte[] txMessage, boolean isSyncCall) throws IOException {
        Response<BaseResp<ResultCommit>> httpRes;
        if (isSyncCall) {
            httpRes = stub.broadcastTxCommit(Numeric.toHexString(txMessage)).execute();
        } else {
            httpRes = stub.broadcastTxAsync(Numeric.toHexString(txMessage)).execute();
        }
        handleRespCommit(httpRes);
        String txHash = httpRes.body().getResult().getTx_hash();
        return txHash;
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
