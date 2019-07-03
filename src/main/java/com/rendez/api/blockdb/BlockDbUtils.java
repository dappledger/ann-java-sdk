package com.rendez.api.blockdb;


import com.rendez.api.bean.model.BlockDbResult;

import com.rendez.api.bean.model.BlockHashResult;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPItem;
import org.ethereum.util.RLPList;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BlockDbUtils {
    //    Height    uint64         `json:"height"`
//    Timestamp uint64         `json:"timestamp"`
//    From      common.Address `json:"from"`
//    Value     []byte         `json:"value"`
//    TxHash    common.Hash    `json:"txhash" gencodec:"required"`
//    Status    uint64         `json:"status"`
    public static BlockDbResult blockDbResultBuilder(String response){
        byte[] origin = Base64.getDecoder().decode(response);

        //byte[] rlp = Numeric.hexStringToByteArray(origin);
        RLPList decodeRlp = RLP.decode2(origin);
        RLPList firstItem = (RLPList) decodeRlp.get(0);

        BlockDbResult result = new BlockDbResult();

        RLPItem Height = (RLPItem) firstItem.get(0);
        BigInteger height = Height.getRLPData().length == 0 ? BigInteger.ZERO : new BigInteger(1, Height.getRLPData());
        result.setHeight(height);

        RLPItem Timestamp = (RLPItem) firstItem.get(1);
        BigInteger timestamp = Timestamp.getRLPData().length == 0 ? BigInteger.ZERO : new BigInteger(1, Timestamp.getRLPData());
        result.setTimestamp(timestamp);

        RLPItem From = (RLPItem) firstItem.get(2);
        String from = Numeric.toHexString((From.getRLPData()));
        result.setFrom(from);

        RLPItem Value = (RLPItem) firstItem.get(3);
        result.setValue(new String(Value.getRLPData()));

        RLPItem TxHash = (RLPItem) firstItem.get(4);
        String hash = Numeric.toHexString(TxHash.getRLPData());
        result.setTxhash(hash);

//        RLPItem Status = (RLPItem) firstItem.get(4);
//        BigInteger status = Status.getRLPData().length == 0 ? BigInteger.ZERO : new BigInteger(1, Status.getRLPData());
//        result.setStatus(status);

        return result;

    }

    public static BlockHashResult blockHashResultBuilder(String response){
        byte[] origin = Base64.getDecoder().decode(response);

        RLPList params = RLP.decode2(origin);
        RLPList txList = (RLPList) params.get(0);
        if (txList.size() > 0) {
            BlockHashResult result = new BlockHashResult();
            List<String> txs = new ArrayList<>();
            txList.forEach(item -> txs.add(com.rendez.api.util.ByteUtil.bytesToHex(item.getRLPData())));
            result.setHashs(txs);
            result.setLength(txs.size());
            return result;
        }

        return new BlockHashResult();

    }
}
