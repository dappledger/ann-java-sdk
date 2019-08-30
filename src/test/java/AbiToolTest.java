import com.genesis.api.AbiTool;
import com.genesis.api.CryptoUtil;
import com.genesis.api.TransactionUtil;
import com.genesis.api.crypto.PrivateKeyECDSA;
import com.genesis.api.crypto.Signature;
import lombok.extern.slf4j.Slf4j;
import org.ethereum.solidity.Abi;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.abi.datatypes.generated.Uint240;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AbiToolTest {

    String abiCode1 = "[{\"constant\":false,\"inputs\":[{\"name\":\"_lotteryId\",\"type\":\"string\"}],\"name\":\"getRaffle\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_lotteryId\",\"type\":\"string\"}],\"name\":\"getWinner\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_lotteryId\",\"type\":\"uint256[2]\"},{\"name\":\"_name\",\"type\":\"string\"},{\"name\":\"_lotteryTime\",\"type\":\"string\"},{\"name\":\"_prizeName\",\"type\":\"string\"},{\"name\":\"_amount\",\"type\":\"uint256\"},{\"name\":\"_lotteryOwner\",\"type\":\"address\"}],\"name\":\"initLottery\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_lotteryId\",\"type\":\"string\"}],\"name\":\"getLottery\",\"outputs\":[{\"name\":\"_reffleNum\",\"type\":\"uint256\"},{\"name\":\"_drawRandom\",\"type\":\"uint256\"},{\"name\":\"_winners\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_lotteryId\",\"type\":\"string\"}],\"name\":\"draw\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_lotteryId\",\"type\":\"string\"},{\"name\":\"_participantAddress\",\"type\":\"address\"},{\"name\":\"_raffleTime\",\"type\":\"string\"},{\"name\":\"_raffleRandom\",\"type\":\"uint256\"}],\"name\":\"raffle\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"}]";
    String abiCode2 = "[{\"constant\":false,\"inputs\":[{\"name\":\"a\",\"type\":\"uint256\"},{\"name\":\"b\",\"type\":\"uint256[]\"}],\"name\":\"queryArray\",\"outputs\":[{\"name\":\"ar\",\"type\":\"uint256[]\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"deposit\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"js\",\"type\":\"string\"}],\"name\":\"queryInfo\",\"outputs\":[{\"name\":\"info\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"from\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"abc\",\"type\":\"string\"}],\"name\":\"Deposit\",\"type\":\"event\"}]";


    @Test
    public void decodeFunction1(){
        AbiTool abiTool = new AbiTool(abiCode1);
        PrivateKeyECDSA privateKey = new PrivateKeyECDSA("16183ac81b7888e9c80aa7c586ed9337478fc6d8bf1f11759a9bb0aed41fc987");
        BigInteger nonce = BigInteger.valueOf(99);
        List<Type> inputParameters = new ArrayList<>();
        inputParameters.add(new StaticArray(new Uint256(1), new Uint256(99)));
        inputParameters.add(new Utf8String("抽奖"));
        inputParameters.add(new Utf8String("201901031836"));
        inputParameters.add(new Utf8String("iphoneXR"));
        inputParameters.add(new Uint256(10));
        inputParameters.add(new Address("0x2b35e1555e3d9e31b0099733cda6a6dfa88bbfdd"));
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Function function = new Function("initLottery", inputParameters, outputParameters);
        RawTransaction tx = TransactionUtil.createCallContractTransaction(nonce, "0xe52c09567254855381395b99decbc84b96de513e", function);
        String rawTxMsg = Numeric.toHexString(TransactionUtil.encode(tx, privateKey));

        // input txmsg here
        // 1. decode tx
        SignedRawTransaction rtx = TransactionUtil.decodeTxMsg(rawTxMsg);
        // 2. decode function
        Function func = abiTool.decodeFunction(rtx.getData());
        for (Type t : func.getInputParameters()){
            log.info("{} {}", t.getTypeAsString(), t.getValue());
        }
        Assert.assertEquals(func.getName(), "initLottery");
        Assert.assertEquals(func.getInputParameters().size(), 6);
        Assert.assertEquals(func.getInputParameters().get(0).getTypeAsString(), "uint256[2]");
        Assert.assertEquals(func.getInputParameters().get(5).getValue().toString(), "0x2b35e1555e3d9e31b0099733cda6a6dfa88bbfdd");
    }


    @Test
    public void decode2(){
        List<Type> inputParameters = new ArrayList<>();
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        Function function = new Function("deposit", inputParameters, outputParameters);
        AbiTool abiTool = new AbiTool(abiCode2);
        Function func = abiTool.decodeFunction(FunctionEncoder.encode(function));
        log.info("{} {}", func.getName(), func.getInputParameters());
    }

}
