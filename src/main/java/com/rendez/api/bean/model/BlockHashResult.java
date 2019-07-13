package com.rendez.api.bean.model;


import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class BlockHashResult {
    private List<BlockHash> hashs;
    private Integer length;
}
