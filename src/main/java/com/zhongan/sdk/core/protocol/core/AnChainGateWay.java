package com.zhongan.sdk.core.protocol.core;

import com.zhongan.sdk.abi.FunctionEncoder;
import com.zhongan.sdk.abi.datatypes.Function;
import com.zhongan.sdk.abi.datatypes.Type;
import com.zhongan.sdk.core.protocol.AnChain;
import com.zhongan.sdk.core.protocol.AnChainService;
import com.zhongan.sdk.core.protocol.core.methods.request.*;
import com.zhongan.sdk.core.protocol.core.methods.response.*;
import com.zhongan.sdk.crypto.AnRawTransaction;
import com.zhongan.sdk.crypto.Credentials;
import com.zhongan.sdk.crypto.TransactionEncoder;
import com.zhongan.sdk.utils.utils.Numeric;
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
    public GenClientVersion genClientVersion() throws Exception{
        Request<GenClientVersion> request = anChain.call("gen_clientVersion",
                GenClientVersion.class,
                null);
        return request.send();
    }


    @Override
    public Result createAccount(CreateAccount createAccount,Credentials credentials) throws Exception{
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
    public Result managerData(ManagerData methodRequest,Credentials credentials) throws Exception{
        AnRawTransaction anRawTransaction = getRawTransaction("manage_data",methodRequest.toString(),methodRequest,credentials);
        Request<Result> call = anChain.call("manage_data",
                Result.class,
                anRawTransaction.getRawTransaction());
        Result result = call.send();
        result.setTxHash(anRawTransaction.getTxHash());
        return result;
    }

    @Override
    public Result createContract(CreateContract createContract ,Credentials credentials) throws Exception{
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
    public Result executeContract(ExecuteContractReq executeContractReq,Credentials credentials) throws Exception{
        ExecuteContract executeContract = new ExecuteContract();
        BeanUtils.copyProperties(executeContract,executeContractReq);
        executeContract.setPayload(FunctionEncoder.encode(new Function(executeContractReq.getMethod_name(), executeContractReq.getInputArgs(), executeContractReq.getOutputArgs())));
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
    public LedgerPagerListResult queryLedgerPaper(PaginationReq paginationReq) throws Exception{
        Request<LedgerPagerListResult> call = anChain.call("query_ledgers",
                LedgerPagerListResult.class,
                paginationReq.getOrder(),
                paginationReq.getLimit(),
                paginationReq.getCursor());
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
    public TransResultList queryAllTrans(PaginationReq paginationReq) throws Exception{
        Request<TransResultList> call = anChain.call("query_payments",
                TransResultList.class,
                paginationReq.getOrder(),
                paginationReq.getLimit(),
                paginationReq.getCursor());
        return call.send();
    }

    @Override
    public TransResultList queryTransByAccount(QueryTransReq queryTransReq) throws Exception{
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
    public TradeResultList queryAllTrades(PaginationReq paginationReq) throws Exception{
        Request<TradeResultList> call = anChain.call("query_transactions",
                TradeResultList.class,
                paginationReq.getOrder(),
                paginationReq.getLimit(),
                paginationReq.getCursor());
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
    public TradeResultList queryTradesByAccount(QueryTradesReq queryTradesReq) throws Exception{
        Request<TradeResultList> call = anChain.call("query_account_transactions",
                TradeResultList.class,
                queryTradesReq.getAddress(),
                queryTradesReq.getOrder(),
                queryTradesReq.getLimit(),
                queryTradesReq.getCursor());
        return call.send();
    }

    @Override
    public TradeResultList queryLedgerTradesByHeight(QueryLedgerReq queryLedgerReq) throws Exception{
        Request<TradeResultList> call = anChain.call("query_ledger_transactions",
                TradeResultList.class,
                queryLedgerReq.getHeight(),
                queryLedgerReq.getOrder(),
                queryLedgerReq.getLimit(),
                queryLedgerReq.getCursor());
        return call.send();
    }

    @Override
    public ContractResult queryContract(QueryContractReq queryContractReq, Credentials credentials) throws Exception {
        QueryContract queryContract = new QueryContract();
        BeanUtils.copyProperties(queryContract,queryContractReq);
        queryContract.setBasefee("0");
        queryContract.setMemo("");
        queryContract.setNonce("0");
        queryContract.setPayload(FunctionEncoder.encode(new Function(queryContractReq.getMethod_name(), queryContractReq.getInputArgs(), queryContractReq.getOutputArgs())));
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
    public Result<Map<String,Value>> queryManagerData(QueryTransReq queryData) throws Exception{
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
