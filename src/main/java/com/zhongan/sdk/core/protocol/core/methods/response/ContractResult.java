package com.zhongan.sdk.core.protocol.core.methods.response;

import com.zhongan.sdk.abi.FunctionReturnDecoder;
import com.zhongan.sdk.abi.TypeReference;
import com.zhongan.sdk.abi.datatypes.Type;
import com.zhongan.sdk.core.protocol.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * User: za-luguiming
 * Date: 2018/11/23
 * Time: 15:26
 */
public class ContractResult extends Response<String> {

    public List<Type> getContractResult(List<TypeReference<Type>> outputParams){
        List<Type> types = null;
        if(StringUtils.isNotEmpty(getResult())){
            types = FunctionReturnDecoder.decode(getResult(), outputParams);
        }
        return types;
    }
}
