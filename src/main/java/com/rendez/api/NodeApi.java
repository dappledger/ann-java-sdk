package com.rendez.api;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.rendez.api.bean.rendez.BaseResp;
import com.rendez.api.bean.rendez.ResultCommit;
import com.rendez.api.bean.rendez.ResultQuery;
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
     * 查询交易
     * @param query
     * @return
     */
    @GET("query")
    Call<BaseResp<ResultQuery>> query(@Query("query") String query);

    /**
     * 查询交易
     * @param query
     * @return
     */
    @GET("transaction")
    Call<BaseResp<ResultQuery>> transaction(@Query("tx") String query);

    /**
     * 根据高度查询交易列表
     * @param height
     * @return
     */
    @GET("block")
    Call<BaseResp<JSONObject>> height(@Query("height") Long height);

}
