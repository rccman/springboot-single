package com.rcc.test.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rcc.test.test.factory.abstractFun.Tire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author By: Jingyan
 * @time: 2019/5/20 15:10
 * @description: 线程池工具类
 */
public class ThreadPoolUtil {

    public volatile static ExecutorService threadPool = null;


    static {
        if (threadPool == null) {
            ThreadFactory threadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("" + "-%d")
                    .setDaemon(true).build();
            BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>(70);
            threadPool = new ThreadPoolExecutor(5, 80, 60L, TimeUnit.SECONDS, blockingQueue, threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        }
    }
    
    
    
    
    private static int universalSyncPoolSize=50;
    private static int universalSyncQueueSize=100;

    public static final Logger logger = LoggerFactory.getLogger(ThreadPoolUtil.class);
    private volatile static ExecutorService universalSyncPool = null;//公用同步线程池
    public volatile static BlockingQueue<Runnable> universalSyncPoolQueue = null;//公用缓冲队列


    public static ExecutorService getUniversalSyncPoolInstance() {
        if (universalSyncPool == null) {
            synchronized (ThreadPoolUtil.class) {
                if (universalSyncPool == null) {
                    universalSyncPoolQueue = new ArrayBlockingQueue<>(universalSyncQueueSize);
                    universalSyncPool = new ThreadPoolExecutor(universalSyncPoolSize, universalSyncPoolSize, 30L, TimeUnit.MINUTES, universalSyncPoolQueue);
                    logger.info("msg1={},,线程池大小={},,缓冲队列大小={}", "实例化一个[公用同步线程池]", universalSyncPoolSize, universalSyncQueueSize);
                }
            }
        }
        return universalSyncPool;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            ThreadPoolUtil.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep((long) Math.random());
//                        TimeUnit.SECONDS.sleep(finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info(Thread.currentThread().getName()+"--"+ finalI);
                }
            });
        }
    }

    

}
