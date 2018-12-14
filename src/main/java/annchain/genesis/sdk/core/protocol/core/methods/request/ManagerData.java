package annchain.genesis.sdk.core.protocol.core.methods.request;

import com.alibaba.fastjson.JSON;
import java.util.Map;

/**
 * User: za-luguiming
 * Date: 2018/11/13
 * Time: 18:14
 */
public class ManagerData extends BaseRequest{

    private Map<String,Value> keyPairList;

    public Map<String, Value> getKeyPairList() {
        return keyPairList;
    }

    public void setKeyPairList(Map<String, Value> keyPairList) {
        this.keyPairList = keyPairList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(keyPairList);
    }
}
