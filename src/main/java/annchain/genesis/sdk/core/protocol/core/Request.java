package annchain.genesis.sdk.core.protocol.core;

import annchain.genesis.sdk.core.protocol.AnChainService;
import rx.Observable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

public class Request<T extends Response> {
    private static AtomicLong nextId = new AtomicLong(0);
    private String jsonrpc = "2.0";
    private String method;
    private List params;
    private Long id;
    private AnChainService anChainService;

    // Unfortunately require an instance of the type too, see
    // http://stackoverflow.com/a/3437930/3211687
    private Class<T> responseType;

    public Request() {
    }

    public Request(String method,List params,
                   AnChainService anChainService, Class<T> type) {
        this.method = method;
        this.params = params;
        this.id=nextId.getAndIncrement();
        this.anChainService = anChainService;
        this.responseType = type;
    }

    public List getParams() {
        return params;
    }

    public void setParams(List params) {
        this.params = params;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T send() throws Exception {
        return anChainService.send(this, responseType);
    }

    public CompletableFuture<T> sendAsync() {
        return  anChainService.sendAsync(this, responseType);
    }

    public Observable<T> observable() {
        return new RemoteCall<>(this::send).observable();
    }

}
