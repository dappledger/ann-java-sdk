package com.genesis.api;

import java.math.BigInteger;

import com.genesis.api.bean.genesis.BaseResp;
import com.genesis.api.bean.genesis.ResultBlock;
import com.genesis.api.bean.genesis.ResultCommit;
import com.genesis.api.bean.genesis.ResultQuery;
import retrofit2.Call;
import retrofit2.http.*;

public interface NodeApi {

    /**
     * 同步广播交易
     * @param tx
     * @return
     */
    @POST("broadcast_tx_commit")
    @FormUrlEncoded
    Call<BaseResp<ResultCommit>> broadcastTxCommit(@Field("tx") String tx);



    /**
     * 异步广播交易
     * @param tx
     * @return
     */
    @POST("broadcast_tx_async")
    @FormUrlEncoded
    Call<BaseResp<ResultCommit>> broadcastTxAsync(@Field("tx") String tx);

    /**
     * 查询
     * @param query
     * @return
     */
    @GET("query")
    Call<BaseResp<ResultQuery>> query(@Query("query") String query);
    
    
    /**
     * 查询交易
     * @param transaction
     * @return
     */
    @GET("transaction")
    Call<BaseResp<ResultQuery>> transaction(@Query("tx") String tx);
    
    /**
     * 查询区块
     * @param block
     * @return
     */
    @GET("block")
    Call<BaseResp<ResultBlock>> block(@Query("height") BigInteger height);
}
