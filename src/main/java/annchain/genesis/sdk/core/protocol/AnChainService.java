package annchain.genesis.sdk.core.protocol;

import annchain.genesis.sdk.core.protocol.core.Request;
import annchain.genesis.sdk.core.protocol.core.Response;

import java.util.concurrent.CompletableFuture;

/**
 * Services API.
 */
public interface AnChainService {
    <T extends Response> T send(
            Request request, Class<T> responseType) throws Exception;

    <T extends Response> CompletableFuture<T> sendAsync(
            Request request, Class<T> responseType);
}
