package com.edu.JavaLearning.io.chat.server;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author tangzh
 * @version 1.0
 * @date 2020/5/18 10:05 PM
 **/
public abstract class AbstractProcessor {

    protected static Map<Integer,AbstractProcessor> processorMap = new ConcurrentHashMap<>();

    private static BlockingQueue<Runnable> chatQueryQueue = new LinkedBlockingDeque<>(10000);

    private static int nCpu = Runtime.getRuntime().availableProcessors();

    public static ExecutorService CHAT_QUERY_EXECUTOR = new ThreadPoolExecutor(
            nCpu,
            2 * nCpu,
            60,
            TimeUnit.SECONDS,
            chatQueryQueue);
}
