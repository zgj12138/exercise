package com.kevin.zookeeper.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁基础实现
 * @author ZGJ
 * create on 2018/1/23 22:09
 **/
public class BaseDistributedLock {

    private final ZkClient client;
    private final String path;
    private final String basePath;
    private final String lockName;
    private static final Integer MAX_RETRY_COUNT = 10;

    public BaseDistributedLock(ZkClient client, String path, String lockName){
        this.client = client;
        this.basePath = path;
        this.path = path.concat("/").concat(lockName);
        this.lockName = lockName;
    }

    private void deleteOurPath(String ourPath) throws Exception {
        client.delete(ourPath);
    }

    private String createLockNode(ZkClient client, String path) throws Exception {
        return client.createEphemeralSequential(path, null);
    }

    private boolean waitToLock(long startMils, Long millsToWait, String ourPath) throws Exception {
        boolean hasLock = false;
        boolean doDelete = false;
        try {
            while (!hasLock) {
                //获取locker节点下的所有顺序节点，并按从小到大排序
                List<String> children = getSortedChildren();
                String sequenceNodeName = ourPath.substring(basePath.length() + 1);
                //计算刚才客户端创创建的顺序节点在locker的所有子节点中排序位置，如果排序为0，则获取到了锁
                int ourIndex = children.indexOf(sequenceNodeName);
                /*
                 如果在getSortedChildren中没有找到之前创建的[临时]顺序节点，这表示可能由于网络闪断而导致
                 Zookeeper认为连接断开而删除了我们创建的节点，此时需要抛出异常，让上一级去处理
                 上一级的做法是捕获该异常，并且执行重试指定的次数 见后面的 attemptLock方法
                 */
                if(ourIndex < 0) {
                    throw new ZkNoNodeException("节点没有找到：" + sequenceNodeName);
                }
                /*
                如果当前客户端创建的节点在locker节点列表中的位置大于0， 表示其他客户端已经
                获取了锁，需要等待其他客户端释放锁
                 */
                boolean isGetLock = ourIndex == 0;
                String pathToWatcch = isGetLock ? null : children.get(ourIndex - 1);

                if(isGetLock) {
                    hasLock = true;
                } else {
                    //如果次小的结点被删除了，则表示当前客户端的节点应该是最小的了
                    String previousSequencePath = basePath.concat("/").concat(pathToWatcch);
                    final CountDownLatch latch = new CountDownLatch(1);
                    final IZkDataListener previousListener = new IZkDataListener() {
                        @Override
                        public void handleDataChange(String s, Object o) throws Exception {
                            //节点改变事件不处理
                        }

                        @Override
                        public void handleDataDeleted(String s) throws Exception {
                            //节点删除事件
                            latch.countDown();
                        }
                    };
                    try {
                        //如果结点不存在则会出现异常
                        client.subscribeDataChanges(previousSequencePath, previousListener);
                        if(millsToWait != null) {
                            millsToWait -= (System.currentTimeMillis() - startMils);
                            startMils = System.currentTimeMillis();
                            if(millsToWait <= 0) {//超时，删除结点
                                doDelete = true;
                                break;
                            }
                            latch.await(millsToWait, TimeUnit.SECONDS);
                        } else {
                            latch.await();
                        }
                    } catch (ZkNoNodeException e) {
                        e.printStackTrace();
                    } finally {
                        //取消订阅
                        client.unsubscribeDataChanges(previousSequencePath, previousListener);
                    }
                }
            }
        } catch (Exception e) {
            if(doDelete) {
                deleteOurPath(ourPath);
            }
        }
        return hasLock;
    }

    /**
     * 获取节点的位置
     * @param str
     * @param lockName
     * @return
     */
    private String getLockNodeNumber(String str, String lockName) {
        int index = str.indexOf(lockName);
        if(index >= 0) {
            index += lockName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }


    private List<String> getSortedChildren() throws Exception {
        try {
            List<String> children = client.getChildren(basePath);
            children.sort((s1, s2) -> getLockNodeNumber(s1, lockName).compareTo(getLockNodeNumber(s2, lockName)));
            return children;
        } catch (ZkNoNodeException e) {
            client.createPersistent(basePath, true);
            return getSortedChildren();
        }
    }

    /**
     * 释放锁
     * @param lockPath
     * @throws Exception
     */
    protected void releaseLock(String lockPath) throws Exception {
        deleteOurPath(lockPath);
    }

    protected String attemptLock(long time, TimeUnit timeUnit) throws Exception {
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (timeUnit != null) ? timeUnit.toMillis(time) : null;

        String ourPath = null;
        boolean hasTheLock = false;
        boolean isDone = false;
        int retryCount = 0;

        //网络闪断需要重新试一试
        while (!isDone) {
            isDone = true;

            try {
                //createLockNode用于在locker（basePath持久节点）下创建客户端要获取锁的[临时]顺序节点
                ourPath = createLockNode(client, path);
                /**
                 * 该方法用于判断自己是否获取到了锁，即自己创建的顺序节点在locker的所有子节点中是否最小
                 * 如果没有获取到锁，则等待其它客户端锁的释放，并且稍后重试直到获取到锁或者超时
                 */
                hasTheLock = waitToLock(startMillis, millisToWait, ourPath);

            } catch (ZkNoNodeException e) {
                if(retryCount++ < MAX_RETRY_COUNT) {
                    isDone = false;
                } else {
                    throw e;
                }
            }
        }
        if(hasTheLock) {
            return ourPath;
        }
        return null;
    }
}
