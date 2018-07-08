package com.kevin.zookeeper.lock;

import java.util.concurrent.TimeUnit;

/**
 * 基于zookeeper实现的分布式锁
 * @author ZGJ
 * create on 2018/1/23 22:03
 **/
public interface DistributeLock {
    /**
     * 获取锁，如果没有得到就等待
     * @throws Exception
     */
    void acquire() throws Exception;

    /**
     * 获取锁，直到超时
     * @param time 超时时间
     * @param timeUnit 时间单位
     * @return 是否获取锁
     * @throws Exception
     */
    boolean acquire(long time , TimeUnit timeUnit) throws Exception;

    /**
     * 释放锁
     * @throws Exception
     */
    void release() throws Exception;
}
