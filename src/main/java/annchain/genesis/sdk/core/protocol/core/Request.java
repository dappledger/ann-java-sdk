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
    private List<MethodRequest> params;
    private Long id;
    private AnChainService anChainService;

    // Unfortunately require an instance of the type too, see
    // http://stackoverflow.com/a/3437930/3211687
    private Class<T> responseType;

    public Request() {
    }

    public Request(String method,List<MethodRequest> params,
                   AnChainService anChainService, Class<T> type) {
        this.method = method;
        this.params = params;
        this.id=nextId.getAndIncrement();
        this.anChainService = anChainService;
        this.responseType = type;
    }

    public List<MethodRequest> getParams() {
        return params;
    }

    public void setParams(List<MethodRequest> params) {
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

    public static class MethodRequest{
        private String basefee;
        private String memo;
        private String from;
        private String to;
        private String nonce;
        private String optype;
        private String operation;
        private String signature;

        public MethodRequest() {

        }

        public MethodRequest(String basefee, String memo, String from, String to, String nonce, String optype, String operation, String signature) {
            this.basefee = basefee;
            this.memo = memo;
            this.from = from;
            this.to = to;
            this.nonce = nonce;
            this.optype = optype;
            this.operation = operation;
            this.signature = signature;
        }

        public String getBasefee() {
            return basefee;
        }

        public void setBasefee(String basefee) {
            this.basefee = basefee;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getOptype() {
            return optype;
        }

        public void setOptype(String optype) {
            this.optype = optype;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
