package annchain.genesis.sdk.core.protocol.core.methods.response;

import annchain.genesis.sdk.core.protocol.core.Response;
import annchain.genesis.sdk.core.protocol.core.methods.request.ContactExist;

public class ContactExistResult extends Response<ContactExist> {
   public ContactExist getStatus(){
       return getResult();
   }
}
