package annchain.genesis.sdk.core.protocol;

import annchain.genesis.sdk.core.protocol.core.JsonRpc2_0AnChain;
import annchain.genesis.sdk.core.protocol.core.Request;
import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.concurrent.ScheduledExecutorService;

/**
 * JSON-RPC Request object building factory.
 */
public interface AnChain<T extends Response>{

    /**
     * Construct a new anChain instance.
     *
     * @param anChainService anChain service instance - i.e. HTTP or IPC
     * @return new anChain instance
     */
    static AnChain build(AnChainService anChainService) {
        return new JsonRpc2_0AnChain(anChainService);
    }

    /**
     * Construct a new anChain instance.
     *
     * @param anChainService anChain service instance - i.e. HTTP or IPC
     * @param pollingInterval polling interval for responses from network nodes
     * @param scheduledExecutorService executor service to use for scheduled tasks.
     *                                 <strong>You are responsible for terminating this thread
     *                                 pool</strong>
     * @return new anChain instance
     */
    static AnChain build(
            AnChainService anChainService, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0AnChain(anChainService, pollingInterval, scheduledExecutorService);
    }

    Request<T> call(String methodName,Class<T> type,Object... rawTransaction);
}
