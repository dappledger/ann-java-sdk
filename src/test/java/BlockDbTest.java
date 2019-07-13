import com.rendez.api.NodeSrv;
import com.rendez.api.bean.model.BlockDbResult;
import com.rendez.api.bean.model.BlockHashResult;
import com.rendez.api.bean.model.BlockdbQueryRequest;
import com.rendez.api.bean.request.BaseRequest;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.PrivateKeyECDSA;
import com.rendez.api.util.RandomNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPList;
import org.junit.BeforeClass;
import org.junit.Test;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BlockDbTest {

    private static final String PRIVATE_KEY = "cf56d88a20eda14c01f39fea6c9ec2600dc3a200fc286074fbfc5a0224e40b5d";
    private static final String value = "ilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youlove youilove y";
    private static final String value2 = "ilove you test";
    //节点服务
    private static NodeSrv nodeSrv;
    private static PrivateKey privateKey;

    @BeforeClass
    public static void init() throws IOException {
        nodeSrv = new NodeSrv("http://localhost:26657");
        privateKey = new PrivateKeyECDSA(PRIVATE_KEY);
    }

    @Test
    public void crypto() {
        PrivateKey prk = new PrivateKeyECDSA(PRIVATE_KEY);
        byte[] b = prk.getAddress().getBytes();
        byte[] cc = Numeric.hexStringToByteArray(prk.getAddress());
        System.out.println(b);
    }


    @Test
    public void account() throws Exception {
        PrivateKey prk = new PrivateKeyECDSA(PRIVATE_KEY);
        System.out.println(prk.toHexString());
        String pubkey = prk.getAddress().startsWith("0x") ? prk.getAddress() : "0x" + prk.getAddress();
        System.out.println(pubkey);
    }

    @Test
    public void Put_Test1() throws Exception {
        String hash = nodeSrv.blockdbPuter(PRIVATE_KEY, "four bottles fo water1550".getBytes(), true);
        System.out.println(hash);
        TimeUnit.SECONDS.sleep(5);

        BlockDbResult value = nodeSrv.blockdbGeter(PRIVATE_KEY,hash);
        System.out.println(value);


    }

    @Test
    public void Put_Test() throws Exception {

        File hf = new File("hash.log");
        FileOutputStream fos = new FileOutputStream(hf, false);
        Double sum = new Double(0);
        for (int i = 0; i < 100000; i++) {
            long s = System.currentTimeMillis();
            String hash = nodeSrv.blockdbPuter(PRIVATE_KEY, value.getBytes(), false);
            long e = System.currentTimeMillis();
            Double feild = new Double(e) - new Double(s);
            sum += feild;
            if (i % 10 == 0) {
                System.out.println(String.format("已处理:%s ;平均响应时间：%s", i, sum / i + 1));

            }
            fos.write((hash + "\n").getBytes());
        }
        fos.close();
    }



    @Test
    public void Get_Test1() throws Exception {
        BlockDbResult hash = nodeSrv.blockdbGeter(PRIVATE_KEY,"527a7fc14ffaaff7f1279faa48eefacd0a9a9ab7f61fd5adb95948bfbb42557d");
        System.out.println(hash);

    }

    @Test
    public void rlp(){
        byte[] rlp = Numeric.hexStringToByteArray("3329993A865DB5185CA5DFBBBCF50F9E219B902C695487E2FA0F3A9EEF391DDB");
        RLPList decodeRlp = RLP.decode2(rlp);
        RLPList firstItem = (RLPList) decodeRlp.get(0);
        System.out.println("aa");
    }

    @Test
    public void Get_Block() throws Exception {
//        BlockdbQueryRequest par = new BlockdbQueryRequest();
//        par.setData("C61D72ED2504743524854132D7FCF841FEA041B3A6051D54CBF44EA529AD11E9");
//        par.setPath("BLOCK");
//        par.setProve(true);
//
//        BaseRequest request = new BaseRequest("abci_query", par);
        BlockHashResult hash = nodeSrv.blockHashs("8ECBFBE88A82ED06051DDD50AE3723E7AB1BB823DA9924F8A14AB2D59CB08ECC");
        System.out.println(hash);

    }

    @Test
    public void testRandomName(){
        System.out.println(RandomNameUtil.generateName("jsonrpc-client-",8));
    }

    @Test
    public void nano() {
        System.out.println(System.nanoTime());
        System.out.println(System.currentTimeMillis());
        double aa = new Double(4) / new Double(7);
        System.out.println(aa
        );
    }


}
