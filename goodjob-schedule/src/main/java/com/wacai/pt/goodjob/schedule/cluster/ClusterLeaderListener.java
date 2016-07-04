package com.wacai.pt.goodjob.schedule.cluster;

/**
 * Created by xuanwu on 16/3/22.
 */
public interface ClusterLeaderListener {
    void gainLeader();

    void loseLeader();
}
