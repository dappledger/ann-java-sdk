package com.rendez.api;

import lombok.Data;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;

import java.util.List;

/**
 * 用户自定义处理event 的callback
 *
 * @param
 */
@Data
public abstract class EventCallBack {

    //默认超时时间 30s
    private static final int DEFAULT_WAITE = 30; // poll 10s

    //超时时间
    private int pollTime;

    //事件格式定义
    private Event event;

    public EventCallBack(int pollTime, Event event) {
        this.pollTime = pollTime;
        this.event = event;
    }

    public EventCallBack(Event event) {
        this.event = event;
        this.pollTime = DEFAULT_WAITE;
    }

    final  void handleLogs(String logData) {
        List<Type> decodeResult = FunctionReturnDecoder.decode(logData, event.getNonIndexedParameters());
        handleEvent(decodeResult);
    }

    /**
     * 具体处理业务逻辑代码
     * @param decodeResult
     */
    public abstract void handleEvent(List<Type> decodeResult);
}
