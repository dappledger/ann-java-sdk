import com.fasterxml.jackson.databind.util.JSONPObject;
import com.rendez.api.NodeSrv;
import com.rendez.api.bean.model.BlockDbResult;
import com.rendez.api.crypto.PrivateKey;
import com.rendez.api.crypto.PrivateKeyECDSA;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BlockDbTest {

    private static final String PRIVATE_KEY = "e19533f586d3356f1c2ecb30d65430cfc15ef37907512a83812454c37aa429b7";
    private static final String value = "ilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youilove youlove youilove y";
    //节点服务
    private static NodeSrv nodeSrv;
    private static PrivateKey privateKey;

    @BeforeClass
    public static void init() throws IOException {
        nodeSrv = new NodeSrv("http://172.28.133.197:46657");
        privateKey = new PrivateKeyECDSA(PRIVATE_KEY);
    }

    @Test
    public void Put_Test1() throws Exception {
        String hash = nodeSrv.blockdbPuter(PRIVATE_KEY, value.getBytes(), false);
        System.out.println(hash);
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
    public void Put_Test_mulity_node() throws Exception {
        ExecutorService executor = new ThreadPoolExecutor(4, 4, 800L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        AtomicInteger sum = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger latch = new AtomicInteger(0);
        executor.execute(new Task(sum,count,new NodeSrv("http://172.28.133.181:46657"),PrivateKeyECDSA.Create().toHexString(),latch));
        executor.execute(new Task(sum,count,new NodeSrv("http://172.28.133.183:46657"),PrivateKeyECDSA.Create().toHexString(),latch));
        executor.execute(new Task(sum,count,new NodeSrv("http://172.28.133.196:46657"),PrivateKeyECDSA.Create().toHexString(),latch));
        executor.execute(new Task(sum,count,new NodeSrv("http://172.28.133.197:46657"),PrivateKeyECDSA.Create().toHexString(),latch));
        while(latch.get() < 4){

        }
        System.out.println(sum.doubleValue()/4);
    }

    class Task implements Runnable{

        AtomicInteger sum;
        AtomicInteger count;
        NodeSrv nodeSrv;
        String privateKey;
        AtomicInteger latch;
        public Task(AtomicInteger sum,AtomicInteger count,NodeSrv nodeSrv,String privateKey,AtomicInteger latch){
            this.count =count;
            this.nodeSrv = nodeSrv;
            this.sum = sum;
            this.privateKey = privateKey;
            this.latch = latch;

        }

        @Override
        public void run() {
            long s = System.currentTimeMillis();
            int num = 100;
            for (int i = 0; i < num; i++) {
                try {
                    String hash = nodeSrv.blockdbPuter(privateKey, value.getBytes(), false);
                    count.addAndGet(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                if (i % 10 == 0) {
//                    System.out.println(String.format("%s = 已处理:%s ;平均响应时间：%s",Thread.currentThread().getName(), count.get(), sum.doubleValue() /( count.doubleValue() + 1)));
//
//                }
            }
            long e = System.currentTimeMillis();
            System.out.println(String.format("%s 处理 %s 次 保存，耗时：%s",Thread.currentThread().getName(),num,e-s));
            sum.addAndGet((int)(e -s));
            latch.addAndGet(1);
        }
    }



    @Test
    public void Get_Test1() throws Exception {
        BlockDbResult hash = nodeSrv.blockdbGeter("0x7c3792c729410174204651ad83323c845db8e851c73daa73465d2c68041a8e54");
        System.out.println(hash);
    }


    @Test
    public void Get_Test() throws Exception {
        Double sum = new Double(0);
        Double ok = new Double(0);
        Scanner scanner = new Scanner(new FileInputStream(new File("hash.log")));
        while (scanner.hasNext()) {
            sum++;
            BlockDbResult hash = nodeSrv.blockdbGeter(scanner.next());
            if ("ilove you".equals(hash.getValue())) {
                ok++;

            }
            if (sum % 10 == 0) {
                System.out.println(String.format("已处理:%s ", sum));
            }
        }
        double rate = ok / sum;
        System.out.println(String.format("成功率 = %s ", rate));
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
