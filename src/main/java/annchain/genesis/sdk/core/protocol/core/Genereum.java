package annchain.genesis.sdk.core.protocol.core;

import annchain.genesis.sdk.core.protocol.core.methods.request.*;
import annchain.genesis.sdk.core.protocol.core.methods.response.*;
import annchain.genesis.sdk.core.protocol.core.methods.request.*;
import annchain.genesis.sdk.core.protocol.core.methods.response.*;
import annchain.genesis.sdk.crypto.Credentials;
import java.util.Map;

/**
 * Core anChain JSON-RPC API.
 */
public interface Genereum {

    GenClientVersion genClientVersion() throws Exception;

    /**
     * 创建账户
     * @param createAccount
     * @return
     */
    Result createAccount(CreateAccount createAccount,Credentials credentials) throws Exception;

    /**
     *  支付转账
     * @param payment
     * @return
     */
    Result payment(Payment payment,Credentials credentials) throws Exception;

    /**
     * 账户数据实体
     * @param managerData
     * @return
     */
    Result managerData(ManagerData managerData,Credentials credentials) throws Exception;

    /**
     * 创建合约
     * @param createContract
     * @return
     */
    Result createContract(CreateContract createContract,Credentials credentials) throws Exception;

    /**
     * 执行合约
     * @param executeContractReq
     * @return
     */
    Result executeContract(ExecuteContractReq executeContractReq,Credentials credentials) throws Exception;

    /**
     * 获取nonce
     * @return
     */
    NonceResult queryNonce(String address) throws Exception;

    /**
     * 查询账户信息
     * @param address
     * @return
     */
    AccountResult queryAccountInfo(String address) throws Exception;

    /**
     * 查询所有账页信息
     * @param paginationReq
     * @return
     */
    LedgerPagerListResult queryLedgerPaper(PaginationReq paginationReq) throws Exception;

    /**
     * 查询指定账页信息
     * @param height
     * @return
     */
    LedgerPagerResult queryLedgerBySequence(Long height) throws Exception;

    /**
     * 查询所有转账
     * @param paginationReq
     * @return
     */
    TransResultList queryAllTrans(PaginationReq paginationReq) throws Exception;

    /**
     * 查询指定账户转账
     * @param queryTransReq
     * @return
     */
    TransResultList queryTransByAccount(QueryTransReq queryTransReq) throws Exception;

    /**
     * 查询指定交易转账
     * @param txHash
     * @return
     */
    TransResult queryTransByTxHash(String txHash) throws Exception;

    /**
     * 查询所有交易信息
     * @param paginationReq
     * @return
     */
    TradeResultList queryAllTrades(PaginationReq paginationReq) throws Exception;

    /**
     * 查询指定交易信息
     * @param txHash
     * @return
     */
    TradeResult queryTradesByTxHash(String txHash) throws Exception;

    /**
     * 查询指定账户交易信息接口
     * @return
     */
    TradeResultList queryTradesByAccount(QueryTradesReq queryTradesReq) throws Exception;

    /**
     * 查询指定账页交易信息接口
     * @return
     */
    TradeResultList queryLedgerTradesByHeight(QueryLedgerReq queryLedgerReq) throws Exception;
    /**
     * 查询合约
     * @param queryContractReq
     * @return
     */
    ContractResult queryContract(QueryContractReq queryContractReq, Credentials credentials) throws Exception;

    /**
     * 查询合约是否存在
     * @param contractAddress
     * @return
     */
    ContactExistResult queryContractStatus(String contractAddress) throws Exception;

    /**
     * 查询合约票据
     * @param txHash
     * @return
     */
    ContractReceiptResult queryContractReceipt(String txHash) throws Exception;

    /**
     * 查询指定账户所有数据实体
     * @param queryData
     * @return
     */
    Result<Map<String,Value>> queryManagerData(QueryTransReq queryData) throws Exception;

    /**
     * 根据Key查询指定账户数据实体
     * @param address
     * @param key
     * @return
     */
    Result<Map<String,Value>> queryManagerDataByKey(String address,String key) throws Exception;

    /**
     * 根据Category查询指定账户数据实体
     * @param address
     * @param category
     * @return
     */
    Result<Map<String,Value>> queryManagerDataByCategory(String address,String category) throws Exception;
}
