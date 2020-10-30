package com.rcc.test.lock;

import com.rcc.test.utils.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: renchaochao
 * @Date: 2020/9/3 11:12
 **/
public class ReentrantLockTest {

    private static final Logger logger = LoggerFactory.getLogger(ReentrantLockTest.class);

    /**
     * ReentrantLock 普通锁
     */
    private static Lock reentrantLock1 = new ReentrantLock();
    /**
     * ReentrantLock 公平锁
     */
    private static Lock reentrantLock2 = new ReentrantLock(true);

    public static void testLock(Lock lock) {
        for (int i = 0; i < 2 ; i++) {
            try {
                lock.lock();
                logger.info("获取到锁");
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }


    public static void main(String[] args) {
//        Lock lock = reentrantLock1;
        Lock lock = reentrantLock2;
        new Thread(()->testLock(lock),"线程1").start();
        new Thread(()->testLock(lock),"线程2").start();
        new Thread(()->testLock(lock),"线程3").start();
        new Thread(()->testLock(lock),"线程4").start();
    }
}
