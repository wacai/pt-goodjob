package com.wacai.pt.goodjob.common.thread;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * Created by xuanwu on 2015/4/7.
 */
public class GoodJobThreadpool {
    private static final long DEFAULT_KEEPALIVE_MINUTES = 30L;

    private ExecutorService   executorService;

    private ThreadFactory     threadFactory;

    public GoodJobThreadpool(String poolName, int corePoolSize, int maximumPoolSize,
                             BlockingQueue<Runnable> workQueue) {
        this(poolName, corePoolSize, maximumPoolSize, DEFAULT_KEEPALIVE_MINUTES, workQueue,
            new AbortPolicy());
    }

    public GoodJobThreadpool(String poolName, int corePoolSize, int maximumPoolSize,
                             BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(poolName, corePoolSize, maximumPoolSize, DEFAULT_KEEPALIVE_MINUTES, workQueue, handler);
    }

    public GoodJobThreadpool(String poolName, int corePoolSize, int maximumPoolSize,
                             long keepAliveMinutes, BlockingQueue<Runnable> workQueue,
                             RejectedExecutionHandler handler) {
        this.threadFactory = new GoodJobThreadFactory(poolName);
        this.executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveMinutes, TimeUnit.MINUTES, workQueue, threadFactory, handler);
    }

    public static GoodJobThreadpool newCachedThreadpool(String poolName) {
        return new GoodJobThreadpool(poolName, 0, Integer.MAX_VALUE,
            new SynchronousQueue<Runnable>());
    }

    public void execute(Runnable run) {
        this.executorService.execute(run);
    }

    public <T> Future<T> submit(Callable<T> call) {
        return this.executorService.submit(call);
    }

    public Future<?> submit(Runnable run) {
        return this.executorService.submit(run);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }
}
