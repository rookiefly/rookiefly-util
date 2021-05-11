package com.rookiefly.commons.lock;

import com.google.common.base.Strings;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

public class FairLock {

    private String zkQurom = "localhost:2181";

    private String lockName = "/mylock";

    private String node; //线程当前节点

    private String ownerNode; //获取锁的最小节点

    private ZooKeeper zk;

    public FairLock() {
        try {
            zk = new ZooKeeper(zkQurom, 6000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("Receive event " + watchedEvent);
                    if (Event.KeeperState.SyncConnected == watchedEvent.getState())
                        System.out.println("connection is established...");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void ensureRootPath() {
        try {
            if (zk.exists(lockName, true) == null) {
                zk.create(lockName, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取锁
     *
     * @return
     * @throws InterruptedException
     */
    public void lock() {
        if (isOwner()) {
            return;
        }
        ensureRootPath();
        try {
            if (node == null) {
                node = zk.create(lockName + "/mylock_", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            }
            List<String> paths = zk.getChildren(lockName, false);
            System.out.println(paths);
            Collections.sort(paths);
            ownerNode = paths.get(0);
            System.out.println(ownerNode + " and path " + node);
            final Thread currentThread = Thread.currentThread();
            if (!Strings.nullToEmpty(node).trim().isEmpty() && !Strings.nullToEmpty(ownerNode).trim().isEmpty() && node.equals(lockName + "/" + ownerNode)) {
                System.out.println(currentThread.getName() + "  get Lock...");
                return;
            }
            String watchNode = null;
            for (int i = paths.size() - 1; i >= 0; i--) {
                if (paths.get(i).compareTo(node.substring(node.lastIndexOf("/") + 1)) < 0) {
                    watchNode = paths.get(i);
                    break;
                }
            }

            if (watchNode != null) {
                final String watchNodeTmp = watchNode;
                Stat stat = zk.exists(lockName + "/" + watchNodeTmp, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                            LockSupport.unpark(currentThread);
                            System.out.println(currentThread.getName() + "  get Lock...");
                            //thread.interrupt();
                        }
                        try {
                            zk.exists(lockName + "/" + watchNodeTmp, true);
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });
                if (stat != null) {
                    System.out.println("Thread " + currentThread.getId() + " waiting for " + lockName + "/" + watchNode);
                }
            }

            LockSupport.park();
//            try {
//                Thread.sleep(50000);
//            } catch (InterruptedException ex) {
//                System.out.println(Thread.currentThread().getName() + " notify");
//                System.out.println(Thread.currentThread().getName() + "  get Lock...");
//                return;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前是不是锁的owner
     */
    public boolean isOwner() {
        return node != null && ownerNode != null && node.equals(ownerNode);
    }

    /**
     * 释放锁
     */
    public void unlock() {
        try {
            System.out.println(Thread.currentThread().getName() + " release Lock...");
            zk.delete(node, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } finally {
            node = null;
        }
    }


    public static void main(String args[]) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 4; i++) {
            service.execute(() -> {
                FairLock test = new FairLock();
                try {
                    test.lock();
                    System.out.println(Thread.currentThread().getName() + " doing");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    test.unlock();
                }
            });
        }
        service.shutdown();
    }

}