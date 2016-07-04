package com.wacai.pt.goodjob.schedule.cluster;

import com.wacai.pt.goodjob.common.exception.ScheduleRuntimeException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanwu on 16/3/22.
 */
public class ZookeeperClusterManager implements ClusterManager {
    private static Logger               logger                = LoggerFactory
                                                                  .getLogger(ZookeeperClusterManager.class);

    private List<ClusterLeaderListener> listeners             = new ArrayList<ClusterLeaderListener>();

    private static final String         DEFAULT_ELECTION_PATH = "/WACAI/PT/GOODJOB/ELECTION/HOROLOGIUM";

    private CuratorFramework            framework;

    private volatile boolean            isLeaderLatchStart    = false;

    private LeaderLatch                 leaderLatch;

    public ZookeeperClusterManager(String zkcluster) {
        try {
            logger.debug("Zookeeper cluster manager's election path is '" + DEFAULT_ELECTION_PATH
                         + "'.");
            logger.info("Start to initiate zookeeper cluster manager with zk servers[" + zkcluster
                    + "].");
            framework = CuratorFrameworkFactory.newClient(zkcluster.trim(), 30000, 5000,
                new ExponentialBackoffRetry(2000, Integer.MAX_VALUE));
            framework.start();
            leaderLatch = new LeaderLatch(framework, DEFAULT_ELECTION_PATH);
            leaderLatch.start();
            isLeaderLatchStart = true;
            leaderLatch.addListener(new LeaderLatchListener() {
                @Override
                public void notLeader() {
                    try {
                        synchronized (this) {
                            if (!ZookeeperClusterManager.this.listeners.isEmpty()) {
                                for (ClusterLeaderListener listener : ZookeeperClusterManager.this.listeners) {
                                    listener.loseLeader();
                                }
                                logger
                                        .info("Succeed to respond lose-leader event, i'm not leader any more.");
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Failed to respond lose-leader event.", e);
//                        //todo 告警
//                        CoeusMonitor.addException(e);
                    }
                }

                @Override
                public void isLeader() {
                    try {
                        synchronized (this) {
                            if (!ZookeeperClusterManager.this.listeners.isEmpty()) {
                                for (ClusterLeaderListener listener : ZookeeperClusterManager.this.listeners) {
                                    listener.gainLeader();
                                }
                                logger
                                        .info("Succeed to respond gain-leader event, i'm the new leader.");
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Failed to respond gain-leader event.", e);
//                        //todo 告警
//                        CoeusMonitor.addException(e);
                    }
                }
            });
            logger.info("Succeed to initiate zookeeper cluster manager.");
        } catch (Exception e) {
            throw new ScheduleRuntimeException("Failed to initiate zookeeper cluster manager.", e);
        }
    }

    @Override
    public void acquireLeader() throws Exception {
        this.leaderLatch.await();
    }

    public void close() throws Exception {
        this.leaderLatch.close();
        this.framework.close();
    }

    public void closeLeaderLatch() throws Exception {
        this.leaderLatch.close();
        isLeaderLatchStart = false;
    }

    public void startLeaderLatch() throws Exception {
        if (!isLeaderLatchStart) {
            this.leaderLatch.start();
        }
    }

    @Override
    public synchronized void addLeaderListener(ClusterLeaderListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }
}
