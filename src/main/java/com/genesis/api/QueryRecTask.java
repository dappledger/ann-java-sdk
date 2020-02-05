package com.genesis.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QueryRecTask {
    private String txHash;
    private EventCallBack eventCallBack;
    private NodeSrv nodeSrv;
}
