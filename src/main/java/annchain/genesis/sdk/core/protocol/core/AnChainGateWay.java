package annchain.genesis.sdk.core.protocol.core;

import annchain.genesis.sdk.abi.datatypes.Type;
import annchain.genesis.sdk.core.protocol.AnChain;
import annchain.genesis.sdk.core.protocol.AnChainService;
import annchain.genesis.sdk.core.protocol.core.methods.request.*;
import annchain.genesis.sdk.core.protocol.core.methods.response.*;
import annchain.genesis.sdk.crypto.AnRawTransaction;
import annchain.genesis.sdk.abi.FunctionEncoder;
import annchain.genesis.sdk.abi.datatypes.Function;
import annchain.genesis.sdk.crypto.Credentials;
import annchain.genesis.sdk.crypto.TransactionEncoder;
import annchain.genesis.sdk.utils.utils.Numeric;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import java.util.List;
import java.util.Map;

/**
 * User: za-luguiming
 * Date: 2018/11/21
 * Time: 17:23
 */
public class AnChainGateWay implements Genereum {

    protected final AnChain anChain;

    public AnChainGateWay(AnChainService anChainService) {
        this.anChain= new JsonRpc2_0AnChain(anChainService);
    }


    @Override
    public Result createAccount(CreateAccount createAccount, Credentials credentials) throws Exception{
        AnRawTransaction anRawTransaction = getRawTransaction("create_account",createAccount.toString(),createAccount,credentials);
        Request<Result> call = anChain.call("create_account",
                Result.class,
                anRawTransaction.getRawTransaction());
        Result result = call.send();
        result.setTxHash(anRawTransaction.getTxHash());
        return result;
    }

    @Override
    public Result payment(Payment payment,Credentials credentials) throws Exception{
        AnRawTransaction anRawTransaction = getRawTransaction("payment",payment.toString(),payment,credentials);
        Request<Result> call = anChain.call("payment",
                Result.class,
                anRawTransaction.getRawTransaction());
        Result result = call.send();
        result.setTxHash(anRawTransaction.getTxHash());
        return result;
    }

    @Override
    public Result managerData(ManagerData methodRequest, Credentials credentials) throws Exception{
        AnRawTransaction anRawTransaction = getRawTransaction("manage_data",methodRequest.toString(),methodRequest,credentials);
        Request<Result> call = anChain.call("manage_data",
                Result.class,
                anRawTransaction.getRawTransaction());
        Result result = call.send();
        result.setTxHash(anRawTransaction.getTxHash());
        return result;
    }

    @Override
    public Result createContract(CreateContract createContract , Credentials credentials) throws Exception{
        ExecuteContract executeContract = new ExecuteContract();
        BeanUtils.copyProperties(executeContract,createContract);
        executeContract.setPayload(createContract.getContractCode()+encodeConstructor(createContract.getConstructorTypes()));
        AnRawTransaction anRawTransaction = getRawTransaction("create_contract",executeContract.toString(),executeContract,credentials);
        Request<Result> call = anChain.call("create_contract",
                Result.class,
                anRawTransaction.getRawTransaction());
        Result result = call.send();
        result.setTxHash(anRawTransaction.getTxHash());
        return result;
    }

    @Override
    public Result executeContract(ContractTrans contractTrans, Credentials credentials) throws Exception{
        ExecuteContract executeContract = new ExecuteContract();
        BeanUtils.copyProperties(executeContract, contractTrans);
        executeContract.setPayload(FunctionEncoder.encode(new Function(contractTrans.getMethod_name(), contractTrans.getInputArgs(), contractTrans.getOutputArgs())));
        AnRawTransaction anRawTransaction = getRawTransaction("execute_contract",executeContract.toString(),executeContract,credentials);
        Request<Result> call = anChain.call("execute_contract",
                Result.class,
                anRawTransaction.getRawTransaction());
        Result result = call.send();
        result.setTxHash(anRawTransaction.getTxHash());
        return result;
    }

    @Override
    public NonceResult queryNonce(String address) throws Exception{
        Request<NonceResult> call =  anChain.call("query_nonce",
                NonceResult.class,
                address);
        return call.send();
    }

    @Override
    public AccountResult queryAccountInfo(String address) throws Exception{
        Request<AccountResult> call = anChain.call("query_account",
                AccountResult.class,
                address);
        return call.send();
    }

    @Override
    public LedgerPagerListResult queryLedgerPaper(Pagination pagination) throws Exception{
        Request<LedgerPagerListResult> call = anChain.call("query_ledgers",
                LedgerPagerListResult.class,
                pagination.getOrder(),
                pagination.getLimit(),
                pagination.getCursor());
        System.out.println(call.getParams());
        return call.send();
    }

    @Override
    public LedgerPagerResult queryLedgerBySequence(Long height) throws Exception{
        Request<LedgerPagerResult> call = anChain.call("query_ledger",
                LedgerPagerResult.class,
                height);
        return call.send();
    }

    @Override
    public TransResultList queryAllTrans(Pagination pagination) throws Exception{
        Request<TransResultList> call = anChain.call("query_payments",
                TransResultList.class,
                pagination.getOrder(),
                pagination.getLimit(),
                pagination.getCursor());
        return call.send();
    }

    @Override
    public TransResultList queryTransByAccount(QueryTrans queryTransReq) throws Exception{
        Request<TransResultList> call = anChain.call("query_account_payments",
                TransResultList.class,
                queryTransReq.getAddress(),
                queryTransReq.getOrder(),
                queryTransReq.getLimit(),
                queryTransReq.getCursor());
        return call.send();
    }

    @Override
    public TransResult queryTransByTxHash(String txHash) throws Exception{
        Request<TransResult> call = anChain.call("query_payment",
                TransResult.class,
                txHash);
        return call.send();
    }

    @Override
    public TradeResultList queryAllTrades(Pagination pagination) throws Exception{
        Request<TradeResultList> call = anChain.call("query_transactions",
                TradeResultList.class,
                pagination.getOrder(),
                pagination.getLimit(),
                pagination.getCursor());
        return call.send();

    }

    @Override
    public TradeResult queryTradesByTxHash(String txHash) throws Exception{
        Request<TradeResult> call = anChain.call("query_transaction",
                TradeResult.class,
                txHash);
        return call.send();
    }

    @Override
    public TradeResultList queryTradesByAccount(QueryTrades queryTradesReq) throws Exception{
        Request<TradeResultList> call = anChain.call("query_account_transactions",
                TradeResultList.class,
                queryTradesReq.getAddress(),
                queryTradesReq.getOrder(),
                queryTradesReq.getLimit(),
                queryTradesReq.getCursor());
        return call.send();
    }

    @Override
    public TradeResultList queryLedgerTradesByHeight(QueryLedger queryLedgerReq) throws Exception{
        Request<TradeResultList> call = anChain.call("query_ledger_transactions",
                TradeResultList.class,
                queryLedgerReq.getHeight(),
                queryLedgerReq.getOrder(),
                queryLedgerReq.getLimit(),
                queryLedgerReq.getCursor());
        return call.send();
    }

    @Override
    public ContractResult queryContract(GetContractResult getContractResult, Credentials credentials) throws Exception {
        QueryContract queryContract = new QueryContract();
        BeanUtils.copyProperties(queryContract, getContractResult);
        queryContract.setBasefee("0");
        queryContract.setMemo("");
        queryContract.setNonce("0");
        queryContract.setPayload(FunctionEncoder.encode(new Function(getContractResult.getMethod_name(), getContractResult.getInputArgs(), getContractResult.getOutputArgs())));
        AnRawTransaction anRawTransaction = getRawTransaction("query_contract",queryContract.toString(),queryContract,credentials);
        Request<ContractResult> call = anChain.call("query_contract",
                ContractResult.class,
                anRawTransaction.getRawTransaction());
        ContractResult result = call.send();
        result.setTxHash(anRawTransaction.getTxHash());
        return result;
    }

    @Override
    public ContactExistResult queryContractStatus(String contractAddress) throws Exception{
        Request<ContactExistResult> call = anChain.call("query_contract_exist",
                ContactExistResult.class,
                contractAddress);
        return call.send();
    }

    @Override
    public ContractReceiptResult queryContractReceipt(String txHash) throws Exception {
        Request<ContractReceiptResult> call = anChain.call("query_receipt",
                ContractReceiptResult.class,
                txHash);
        return call.send();
    }

    @Override
    public Result<Map<String,Value>> queryManagerData(QueryTrans queryData) throws Exception{
        Request<Result<Map<String,Value>>> call = anChain.call("query_account_managedatas",
                Result.class,
                queryData.getAddress(),
                queryData.getOrder(),
                queryData.getLimit(),
                queryData.getCursor());
        return call.send();
    }

    @Override
    public Result<Map<String,Value>> queryManagerDataByKey(String address, String key) throws Exception{
        Request<Result<Map<String,Value>>> call = anChain.call("query_account_managedata",
                Result.class,
                address,key);
        return call.send();
    }

    @Override
    public Result<Map<String,Value>> queryManagerDataByCategory(String address, String category) throws Exception {
        Request<Result<Map<String,Value>>> call = anChain.call("query_account_category_managedata",
                Result.class,
                address,category);
        return call.send();
    }


    /**
     * get raw transaction
     * @param operType
     * @param operation
     * @param request
     * @param credentials
     * @return
     * @throws Exception
     */
    private AnRawTransaction getRawTransaction(String operType, String operation, BaseRequest request, Credentials credentials) throws Exception{
        AnRawTransaction anRawTransaction = new AnRawTransaction(request.getBasefee(),
                request.getMemo(),request.getFrom(),request.getTo(),request.getNonce(),operType,operation);
        if(credentials != null) {
            anRawTransaction.setSignature(Numeric.toHexString(TransactionEncoder.signMessage(anRawTransaction, credentials)));
        }
        anRawTransaction.setRawTransaction(Numeric.toHexString(TransactionEncoder.encode(anRawTransaction,false)));
        return anRawTransaction;
    }

    /**
     *  encode constructor
     * @param types
     * @return
     */
    private String encodeConstructor(List<Type> types){
        if(CollectionUtils.isEmpty(types)){
            return "";
        }
        String encodedConstructor =
                FunctionEncoder.encodeConstructor(types);
        return encodedConstructor;
    }
}
