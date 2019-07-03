package com.rendez.api;

import com.rendez.api.bean.rendez.BaseResp;
import com.rendez.api.bean.rendez.ResultABCIQuery;
import com.rendez.api.bean.rendez.BaseResult;
import com.rendez.api.bean.request.BaseRequest;
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
    Call<BaseResp<BaseResult>> broadcastTxCommit(@Field("tx") String tx);

    /**
     * 异步广播交易
     * @param tx
     * @return
     */
    @POST("broadcast_tx_async")
    @FormUrlEncoded
    Call<BaseResp<BaseResult>> broadcastTxAsync(@Field("tx") String tx);

    /**
     * tendermint abci_query
     * @param request
     * @return
     */
    @POST("/")
    Call<BaseResp<ResultABCIQuery>> abciquery(@Body BaseRequest request);

}
