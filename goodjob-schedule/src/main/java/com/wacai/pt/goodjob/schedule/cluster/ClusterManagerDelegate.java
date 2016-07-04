package com.wacai.pt.goodjob.schedule.cluster;

import com.wacai.pt.goodjob.schedule.SchedulerConstants;

/**
 * Created by xuanwu on 16/3/22.
 */
public class ClusterManagerDelegate implements ClusterManager {

    private ClusterManager clusterManager;

    public ClusterManagerDelegate(ClusterManager clusterManager) {
        this.clusterManager = clusterManager;
    }

    @Override
    public void acquireLeader() throws Exception {
        String forceLeader = System.getProperty(SchedulerConstants.SCHEDULER_CLUSTER_FORCE_LEADER);
        if (!"true".equalsIgnoreCase(forceLeader)) {
            clusterManager.acquireLeader();
        }
    }

    @Override
    public void addLeaderListener(ClusterLeaderListener listener) {
        clusterManager.addLeaderListener(listener);
    }

    @Override
    public void closeLeaderLatch() throws Exception {
        clusterManager.closeLeaderLatch();
    }

    @Override
    public void startLeaderLatch() throws Exception {
        clusterManager.startLeaderLatch();
    }
}
