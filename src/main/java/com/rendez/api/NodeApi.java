package com.rendez.api;

import com.rendez.api.bean.rendez.BaseResp;
import com.rendez.api.bean.rendez.ResultCommit;
import com.rendez.api.bean.rendez.ResultQuery;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NodeApi {

    /**
     * 同步广播交易
     * @param tx
     * @return
     */
    @GET("broadcast_tx_commit")
    Call<BaseResp<ResultCommit>> broadcastTxCommit(@Query("tx") String tx);



    /**
     * 异步广播交易
     * @param tx
     * @return
     */
    @GET("broadcast_tx_async")
    Call<BaseResp<ResultCommit>> broadcastTxAsync(@Query("tx") String tx);

    /**
     * 查询交易
     * @param query
     * @return
     */
    @GET("query")
    Call<BaseResp<ResultQuery>> query(@Query("query") String query);

}
