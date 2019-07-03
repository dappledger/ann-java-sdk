package com.rendez.api.bean.rendez;

import lombok.Data;

@Data
public class ResponseQuery{
//     type ResponseQuery struct {
//         Code uint32 `protobuf:"varint,1,opt,name=code,proto3" json:"code,omitempty"`
//         // bytes data = 2; // use "value" instead.
//         Log                  string        `protobuf:"bytes,3,opt,name=log,proto3" json:"log,omitempty"`
//         Info                 string        `protobuf:"bytes,4,opt,name=info,proto3" json:"info,omitempty"`
//         Index                int64         `protobuf:"varint,5,opt,name=index,proto3" json:"index,omitempty"`
//         Key                  []byte        `protobuf:"bytes,6,opt,name=key,proto3" json:"key,omitempty"`
//         Value                []byte        `protobuf:"bytes,7,opt,name=value,proto3" json:"value,omitempty"`
//         Proof                *merkle.Proof `protobuf:"bytes,8,opt,name=proof" json:"proof,omitempty"`
//         Height               int64         `protobuf:"varint,9,opt,name=height,proto3" json:"height,omitempty"`
//         Codespace            string        `protobuf:"bytes,10,opt,name=codespace,proto3" json:"codespace,omitempty"`
//         XXX_NoUnkeyedLiteral struct{}      `json:"-"`
//         XXX_unrecognized     []byte        `json:"-"`
//         XXX_sizecache        int32         `json:"-"`
//     }

     int code;
     String log;
     String info;
     String value;
}