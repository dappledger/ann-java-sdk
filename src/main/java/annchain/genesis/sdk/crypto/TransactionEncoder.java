package annchain.genesis.sdk.crypto;

import annchain.genesis.sdk.core.protocol.core.methods.request.NodeUpdate;
import annchain.genesis.sdk.rlp.RlpEncoder;
import annchain.genesis.sdk.rlp.RlpList;
import annchain.genesis.sdk.rlp.RlpString;
import annchain.genesis.sdk.rlp.RlpType;
import annchain.genesis.sdk.utils.crypto.Hash;
import annchain.genesis.sdk.utils.utils.Bytes;
import annchain.genesis.sdk.utils.utils.Numeric;

import java.util.ArrayList;
import java.util.List;


public class TransactionEncoder {

    public static byte[] signMessage(AnRawTransaction anRawTransaction, Credentials credentials) {
        byte[] encodedTransaction = encode(anRawTransaction,true);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());
        anRawTransaction.setTxHash(Numeric.toHexString(Hash.sha3(encodedTransaction)));
        return signatureData.getData();
    }

    public static byte[] encode(AnRawTransaction anRawTransaction,boolean signature) {
        return encode(anRawTransaction,signature, null);
    }

    private static byte[] encode(AnRawTransaction anRawTransaction,boolean signature, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(anRawTransaction,signature, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List<RlpType> asRlpValues(
            AnRawTransaction anRawTransaction,boolean signature, Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(anRawTransaction.getBasefee()));
        result.add(RlpString.create(anRawTransaction.getNonce()));
        result.add(RlpString.create(anRawTransaction.getMemo()));
        result.add(RlpString.create(anRawTransaction.getOptype()));
        result.add(RlpString.create(anRawTransaction.getFrom()));
        result.add(RlpString.create(anRawTransaction.getTo()==null ? "" : anRawTransaction.getTo()));
        result.add(RlpString.create(anRawTransaction.getOperation()));

        if(!signature) {
            String signatureValue = anRawTransaction.getSignature();
            if (signatureValue != null && signatureValue.length() > 0) {
                result.add(RlpString.create(signatureValue));
            }
        }

        if (signatureData != null) {
            result.add(RlpString.create(signatureData.getV()));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }

    public static byte[] encode(NodeUpdate nodeUpdate) {
        List<RlpType> values = asRlpValues(nodeUpdate);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }
    static List<RlpType> asRlpValues(
            NodeUpdate nodeUpdate) {
        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(nodeUpdate.getIsCA()));
        result.add(RlpString.create(nodeUpdate.getPubkey()));
        result.add(RlpString.create(nodeUpdate.getSigs()));
        result.add(RlpString.create(nodeUpdate.getOpcode()));
        result.add(RlpString.create(nodeUpdate.getRpc_address()));
        return result;
    }
}
