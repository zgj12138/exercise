package com.kevin.bio1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 伪异步io处理线程池
 * @author ZGJ
 * create on 2017/10/28 22:46
 **/
public class TimeServerHandlerExecutePool {

    private ExecutorService executorService;

    public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 120L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize));
    }

    public void execute(Runnable task) {
        executorService.execute(task);
    }
}
