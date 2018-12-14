package com.zhongan.sdk.core.protocol.core;

import com.zhongan.sdk.core.protocol.AnChain;
import com.zhongan.sdk.core.protocol.AnChainService;
import com.zhongan.sdk.core.utils.Async;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;

/**
 * JSON-RPC 2.0 factory implementation.
 */
public class JsonRpc2_0AnChain<T extends Response> implements AnChain<T> {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final AnChainService anChainService;
    private final long blockTime;

    public JsonRpc2_0AnChain(AnChainService anChainService) {
        this(anChainService, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0AnChain(
            AnChainService anChainService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.anChainService = anChainService;
        this.blockTime = pollingInterval;
    }

    @Override
    public Request<T> call(String methodName,Class<T> type,Object... rawTransaction) {
        return new Request(
                methodName,
                rawTransaction!=null?Arrays.asList(rawTransaction):Collections.emptyList(),
                anChainService,
                type);
    }
}
