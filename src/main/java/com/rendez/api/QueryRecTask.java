package com.rendez.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QueryRecTask {

    private String txHash;
    private EventCallBack eventCallBack;
    private NodeSrv nodeSrv;
}
