package com.wacai.pt.goodjob.schedule.cluster;

/**
 * Created by xuanwu on 16/3/22.
 */
public interface ClusterManager {
    void acquireLeader() throws Exception;

    void addLeaderListener(ClusterLeaderListener listener);

    void closeLeaderLatch() throws Exception;

    void startLeaderLatch() throws Exception;
}
