package annchain.genesis.sdk.utils.utils;


import annchain.genesis.sdk.core.protocol.AnChain;
import annchain.genesis.sdk.core.protocol.core.EventTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class EventUtils {
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(30, 30, 0l, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(10000));

    public static void eventCall(AnChain anChain, EventTask eventTask){
        executor.execute(eventTask);
    }
}
