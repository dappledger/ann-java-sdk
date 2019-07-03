package com.rendez.api.bean.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockdbQueryRequest {
    private String path;
    private String data;
//    private Long height;
//    private boolean prove;
}
