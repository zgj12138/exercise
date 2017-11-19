package com.kevin.zookeeper.demo1;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author ZGJ
 * create on 2017/11/11 22:29
 **/
public class ZookeeperConnectWithSid implements Watcher {
    private static final String HOST = "119.29.78.93:2181";
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(HOST, 5000, new ZookeeperConnect());
        System.out.println(zooKeeper.getState());
        connectedSemaphore.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] password = zooKeeper.getSessionPasswd();

        //使用错误的sessionId和密码
        zooKeeper = new ZooKeeper(HOST, 5000, new ZookeeperConnectWithSid(), 1L, "test".getBytes());
        //使用正确的sessionId和密码连接
        zooKeeper = new ZooKeeper(HOST, 5000, new ZookeeperConnectWithSid(), sessionId, password);
        Thread.sleep(10000);
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event: " + watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }

    }
}
