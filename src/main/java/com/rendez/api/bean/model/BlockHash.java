package com.rendez.api.bean.model;

import com.rendez.api.bean.enums.OpEnum;
import lombok.Data;

@Data
public class BlockHash {
    private String txHash;
    private OpEnum op;
}
