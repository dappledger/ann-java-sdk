package annchain.genesis.sdk.core.protocol.core;


import annchain.genesis.sdk.abi.FunctionReturnDecoder;
import annchain.genesis.sdk.abi.datatypes.Type;
import annchain.genesis.sdk.core.protocol.AnChain;
import annchain.genesis.sdk.core.protocol.core.methods.request.EventCallBack;
import annchain.genesis.sdk.core.protocol.core.methods.response.ContractReceipt;
import annchain.genesis.sdk.core.protocol.core.methods.response.ContractReceiptResult;
import annchain.genesis.sdk.core.protocol.core.methods.response.EventLog;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import java.util.List;
import java.util.Map;

public class EventTask implements Runnable{
    private AnChain chain;
    private String txHash;
    private EventCallBack eventCallBack;
    //默认超时时间 30s
    private static final int DEFAULT_WAITE = 30; // poll 10s
    private static final int Gape = 3000;
    public EventTask(String txHash, AnChain anChain,EventCallBack eventCallBack) {
        this.txHash = txHash;
        this.eventCallBack = eventCallBack;
        this.chain = anChain;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public EventCallBack getEventCallBack() {
        return eventCallBack;
    }

    public void setEventCallBack(EventCallBack eventCallBack) {
        this.eventCallBack = eventCallBack;
    }

    public AnChain getChain() {
        return chain;
    }

    public void setChain(AnChain chain) {
        this.chain = chain;
    }

    @Override
    public void run() {
        long end = System.currentTimeMillis() + DEFAULT_WAITE * 1000;
        Map<String,EventCallBack.EventVo> eventVoList = eventCallBack.getEvents();
        if(MapUtils.isEmpty(eventVoList)){
            return;
        }
        while (System.currentTimeMillis() < end) {
            try {
                Thread.sleep(Gape);
                Request<ContractReceiptResult> call = chain.call("query_receipt",
                        ContractReceiptResult.class,
                        txHash);
                ContractReceiptResult contractReceiptResult = call.send();
                if (contractReceiptResult != null && contractReceiptResult.getReceiptResult()!=null) {
                    ContractReceipt contractReceipt = contractReceiptResult.getReceiptResult();
                    List<EventLog> eventLogs =  contractReceipt.getLogs();
                    if(CollectionUtils.isEmpty(eventLogs)){
                        continue;
                    }
                    for (EventLog eventLog : eventLogs){
                        EventCallBack.EventVo eventVo = eventVoList.get(eventLog.getTopics().get(0));
                        if(eventVo != null){
                            List<Type> types = FunctionReturnDecoder.decode(eventLog.getData(),eventVo.getTypes());
                            eventCallBack.eventCall(eventVo.getEventName(),types);
                        }
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}