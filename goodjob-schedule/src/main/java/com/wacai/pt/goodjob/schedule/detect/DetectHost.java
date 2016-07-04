package com.wacai.pt.goodjob.schedule.detect;

import com.wacai.pt.goodjob.remote.bean.ExecuteRequest;
import com.wacai.pt.goodjob.remote.service.JobExecuteService;
import com.wacai.pt.goodjob.schedule.adaptor.SubJobExecuteAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuanwu on 16/6/8.
 */
public class DetectHost {
    private static final Logger            logger     = LoggerFactory.getLogger(DetectHost.class);

    private static Map<String, DetectBean> taskMap    = new ConcurrentHashMap<>(1024);

    private static volatile DetectHost     detectHost = null;
    private final static Object            obj        = new Object();

    private DetectHost() {
        Thread t = new Thread(new DetectHost.HostProcess());
        t.setDaemon(true);
        t.start();
        logger.info("HostProcess Thread start.");
    }

    public static DetectHost getDetectHost() {
        if (detectHost == null) {
            synchronized (obj) {
                if (detectHost == null) {
                    detectHost = new DetectHost();
                }
            }
        }

        return detectHost;
    }

    public Map<String, DetectBean> getTaskMap() {
        return taskMap;
    }

    class HostProcess implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000 * 60 * 1L);
            } catch (InterruptedException e) {

            }

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    long startTime = System.currentTimeMillis();
                    long pause = 0;
                    for (Map.Entry<String, DetectBean> entry : taskMap.entrySet()) {
                        if (entry.getValue().getTask_state().intValue() == 1 || (System.currentTimeMillis() - entry.getValue().getCreateTime() < 1000 * 60 * 60 * 24)){
                            detect(Integer.valueOf(entry.getKey()), entry.getValue().getTaskGroup());
                            Thread.sleep(100L);
                            pause ++;
                        }
                    }
                    logger.info("-------)------)----)---)--)-) 执行机探测 耗时:{}ms, 停顿:{}ms", System.currentTimeMillis() - startTime - pause * 100L, pause * 100L);
                    Thread.sleep(1000 * 60 * 5L);
                } catch (Throwable t) {
                    logger.warn("HostProcess failure:", t);
                }
            }
        }
    }

    public void detect(final Integer taskId, final String taskGroup){
        detect(taskId, taskGroup, false);
    }

    public void detect(final Integer taskId, final String taskGroup, final boolean manual) {
        try {
            JobExecuteService jobExecuteService = SubJobExecuteAdaptor.getJobExecuteService(taskGroup, true);
            ExecuteRequest executeRequest = new ExecuteRequest();
            executeRequest.setTaskId(taskId);
            if (manual){
                executeRequest.setTaskType(-1);
            }
            jobExecuteService.execute(executeRequest);
        } catch (Throwable t) {
            //ignore
        }
    }
}
