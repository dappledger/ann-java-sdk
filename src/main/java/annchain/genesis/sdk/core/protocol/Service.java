package annchain.genesis.sdk.core.protocol;

import annchain.genesis.sdk.core.protocol.core.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import annchain.genesis.sdk.core.protocol.core.Response;
import annchain.genesis.sdk.core.utils.Async;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

/**
 * Base service implementation.
 */
public abstract class Service implements AnChainService {

    protected final ObjectMapper objectMapper;

    public Service(boolean includeRawResponses) {
        objectMapper = ObjectMapperFactory.getObjectMapper(includeRawResponses);
    }

    protected abstract InputStream performIO(String payload) throws IOException;

    @Override
    public <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException {
        String payload = objectMapper.writeValueAsString(request);
        try (InputStream result = performIO(payload)) {
            if (result != null) {
                T response = objectMapper.readValue(result, responseType);
                return response;
            } else {
                return null;
            }
        }
    }

    @Override
    public <T extends Response> CompletableFuture<T> sendAsync(
            Request jsonRpc20Request, Class<T> responseType) {
        return Async.run(() -> send(jsonRpc20Request, responseType));
    }
}
