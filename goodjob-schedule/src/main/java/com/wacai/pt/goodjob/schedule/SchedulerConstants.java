package com.wacai.pt.goodjob.schedule;

/**
 * Created by xuanwu on 16/3/22.
 */
public interface SchedulerConstants {
    String SCHEDULER_PREFIX               = "goodjob-scheduler.";

    String SCHEDULER_CLUSTER_ZK_SERVERS   = SCHEDULER_PREFIX + "cluster.zookeeper.servers";
    String SCHEDULER_CLUSTER_ZK_ELECPATH  = SCHEDULER_PREFIX + "cluster.zookeeper.electionPath";
    String SCHEDULER_CLUSTER_FORCE_LEADER = SCHEDULER_PREFIX + "cluster.forceLeader";
}
