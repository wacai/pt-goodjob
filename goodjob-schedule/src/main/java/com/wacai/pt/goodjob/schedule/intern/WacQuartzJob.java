package com.wacai.pt.goodjob.schedule.intern;

import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.schedule.util.SpringContextHolder;
import com.wacai.pt.goodjob.schedule.util.ThreadUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuanwu on 16/3/23.
 */
public class WacQuartzJob implements Job {
    private static final Logger    logger     = LoggerFactory.getLogger(WacQuartzJob.class);
    private static JsonMapper      jsonMapper = JsonMapper.getNonNullBinder();
    private static ScheduleExecute scheduleExecute;

    static {
        scheduleExecute = SpringContextHolder.getBean("scheduleExecute");
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String, Object> contextMap = new HashMap<>();
        Long nextFireTime = jobExecutionContext.getNextFireTime().getTime();
        Long schedTime = jobExecutionContext.getScheduledFireTime().getTime();
        contextMap.put("taskId", jobExecutionContext.getMergedJobDataMap().get("taskId"));
        contextMap.put("taskName", jobExecutionContext.getMergedJobDataMap().get("taskName"));
        contextMap.put("execType", jobExecutionContext.getMergedJobDataMap().get("execType"));
        contextMap.put("preFireTime", schedTime);
        contextMap.put("nextFireTime", nextFireTime);

        jobExecute(contextMap);
    }

    public void jobExecute(final Map<String, Object> contextMap) {
        ThreadUtil.schedThreadpool.submit(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                logger.info("===========execute========================= contextMap:{}",
                        jsonMapper.toJson(contextMap));
                try {
                    scheduleExecute.rpcExecute(contextMap);
//                    //todo 统计
//                    CoeusMonitor.addNormal(System.currentTimeMillis() - startTime);
                } catch (Throwable e) {
                    logger.error("execute error:{}, contextMap:{}", e, jsonMapper.toJson(contextMap));
//                    //todo 告警
//                    CoeusMonitor.addException("Job execute error", e, jsonMapper.toJson(contextMap));
                }
            }
        });

    }
}
