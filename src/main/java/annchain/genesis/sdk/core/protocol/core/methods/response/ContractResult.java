package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.abi.datatypes.Type;
import annchain.genesis.sdk.abi.FunctionReturnDecoder;
import annchain.genesis.sdk.abi.TypeReference;
import annchain.genesis.sdk.core.protocol.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ContractResult extends Response<String> {

    public List<Type> getContractResult(List<TypeReference<Type>> outputParams){
        List<Type> types = null;
        if(StringUtils.isNotEmpty(getResult())){
            types = FunctionReturnDecoder.decode(getResult(), outputParams);
        }
        return types;
    }
}
