package com.rendez.api;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.ethereum.vm.LogInfo;

import java.util.List;

@Slf4j
public class LogObserver implements Observer<QueryRecTask> {

    //1s 抓一次
    private static final long Gape = 1000;

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(QueryRecTask task) {
        log.debug("get task {}", task);
        long end = System.currentTimeMillis() + task.getEventCallBack().getPollTime() * 1000;
        while (System.currentTimeMillis() < end) {
            try {
                List<LogInfo> recp = task.getNodeSrv().queryReceipt(task.getTxHash());
                if (recp != null) {
                    recp.forEach(log -> task.getEventCallBack().handleLogs(Hex.toHexString(log.getData())));
                    return;
                }
            } catch (Exception e) {
                log.warn("LogObserver queryReceipt", e);
            }

            try {
                // always sleep if receipt not found
                Thread.sleep(Gape);
            } catch (InterruptedException ignored) {
            }
        }

        log.warn("time out for task {}", task);

    }

    @Override
    public void onError(Throwable e) {
        log.error("error on observe ", e);
    }

    @Override
    public void onComplete() {
    }
}
