package com.zhongan.sdk.core.protocol.core.methods.request;

import com.zhongan.sdk.core.protocol.core.Response;

public class ContactExistResult extends Response<ContactExist> {
   public ContactExist getStatus(){
       return getResult();
   }
}
