package com.rendez.api.bean.enums;

/**
 * 操作枚举：PUT((byte) 0x01, "PUT"), GET((byte) 0x02, "GET")
 */
public enum OpEnum {
    PUT((byte) 0x01, "PUT"), GET((byte) 0x02, "GET"), DEFAULT((byte) 0x03, "DEFAULT");

    private byte opcode;
    private String desc;

    OpEnum(byte opcode, String desc) {
        this.opcode = opcode;
        this.desc = desc;
    }

    public byte getOpcode(){
        return opcode;

    }

    public String getDesc(){
        return desc;
    }

    public static OpEnum valueOf(byte op){
        switch (op) {
            case 0x01:
                return OpEnum.PUT;
            case 0x02:
                return OpEnum.GET;
        }
        return OpEnum.DEFAULT;
    }
}
