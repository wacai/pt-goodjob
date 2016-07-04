package com.wacai.pt.goodjob.schedule.intern;

import com.wacai.pt.goodjob.common.exception.ScheduleRuntimeException;
import com.wacai.pt.goodjob.schedule.SchedulerBoot;
import com.wacai.pt.goodjob.schedule.adaptor.SubJobExecuteAdaptor;
import com.wacai.pt.goodjob.schedule.cluster.ClusterLeaderListener;
import com.wacai.pt.goodjob.schedule.cluster.ClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xuanwu on 16/3/23.
 */
@Component("schedulerBoot")
public class SchedulerBootImpl implements SchedulerBoot {
    private static Logger    logger       = LoggerFactory.getLogger(SchedulerBootImpl.class);

    private volatile boolean closed       = false;
    private volatile boolean shuttingDown = false;
    @Autowired
    private ClusterManager   clusterManager;
    @Autowired
    private SchedualManager  schedualManager;

    @Override
    public void start() {
        if (shuttingDown || closed) {
            throw new ScheduleRuntimeException(
                "The scheduler cannot be restarted after been stopped.");
        }

        logger.info("Blocking to acquire the leadership of the scheduler cluster.");
        boolean leaderAcquired = false;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                clusterManager.acquireLeader();
                leaderAcquired = true;
                break;
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                    return;
                }
                if (shuttingDown || closed) {
                    return;
                }
                try {
                    logger.warn("Failed to acquire the leadership, sleep 5 seconds to re-acquire.",
                        e);
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (shuttingDown || closed || !leaderAcquired) {
            logger
                .warn("SchedulerBoot start process is terminated with shutting down or interruption.");
            return;
        }

        logger.info("Acquired the leadership, go on starting process.");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //
        }
        startAllService();

        clusterManager.addLeaderListener(new ClusterLeaderListener() {
            @Override
            public void loseLeader() {
                logger.warn("************************** schedule loseLeader.");
                SchedulerBootImpl.this.destroyAllService();
//                //todo 告警
//                CoeusMonitor.addException("goodjob.sched.leader", "loseLeader", "loseLeader", new Exception("Succeed to respond lose-leader event, i'm not leader any more."), null, Constants.AlarmLevel.IMM_ALARM);
            }

            @Override
            public void gainLeader() {
                logger.warn("************************** schedule gainLeader.");
                SchedulerBootImpl.this.startAllService();
//                //todo 告警
//                CoeusMonitor.addException("goodjob.sched.leader", "gainLeader", "gainLeader", new Exception("Succeed to respond gain-leader event, i'm the new leader."), null, Constants.AlarmLevel.IMM_ALARM);
            }
        });

        logger.info("The scheduler is started.");
    }

    private void startAllService(){
        try {
            SubJobExecuteAdaptor.startDubboService();
            schedualManager.start();
            clusterManager.startLeaderLatch();
        }catch (Throwable t){
            logger.error("******************  start all service error: ", t);
//            //todo 告警
//            CoeusMonitor.addException("goodjob.sched.start", "startAllService", t.getMessage(), t, null, Constants.AlarmLevel.IMM_ALARM);
            destroyAllService();
            try {
                clusterManager.closeLeaderLatch();
            }catch (Exception e){
                logger.error("******************  closeLeaderLatch error: ", t);
            }
        }

    }

    private void destroyAllService() {
        schedualManager.destroy();
        SubJobExecuteAdaptor.destroyConsumer();
        SubJobExecuteAdaptor.destroyProvider();
    }

    @Override
    public void stop() {
        logger.error("************* stop **************");
        if (shuttingDown || closed) {
            return;
        }
        shuttingDown = true;

        logger.info("Scheduler is shutting down.");
        destroyAllService();
        closed = true;
        logger.info("Scheduler is shut down successfully.");
    }
}
