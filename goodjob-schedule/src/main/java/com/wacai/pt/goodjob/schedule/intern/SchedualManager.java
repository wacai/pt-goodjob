package com.wacai.pt.goodjob.schedule.intern;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.schedule.detect.DetectBean;
import com.wacai.pt.goodjob.schedule.detect.DetectHost;
import com.wacai.pt.goodjob.schedule.mapper.TaskConfigMapper;
import com.wacai.pt.goodjob.schedule.mapper.TaskHostMapper;
import com.wacai.pt.goodjob.schedule.model.TaskConfigModel;
import com.wacai.pt.goodjob.schedule.util.HostsUtil;
import com.wacai.pt.goodjob.schedule.vo.HostVo;
import com.wacai.pt.goodjob.schedule.vo.TaskConfigVo;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xuanwu on 16/3/23.
 */
@Component("schedualManager")
public class SchedualManager implements DisposableBean {
    private static final Logger logger    = LoggerFactory.getLogger(SchedualManager.class);

    @Autowired
    private Scheduler           scheduler;

    @Autowired
    private TaskConfigMapper    taskConfigMapper;

    @Autowired
    private TaskHostMapper      taskHostMapper;

    public void destroy() {
        //Quartz 注销
        if (scheduler != null) {
            try {
                scheduler.clear();
                logger.info("============= scheduler clear.");
            } catch (Throwable e) {
                logger.error("****************** scheduler clear failed, error message is:", e);
//                //todo 告警
//                CoeusMonitor.addException(e);
                try {
                    scheduler.shutdown();
                } catch (Exception ex) {
                    logger
                        .error("****************** scheduler clear failed, error message is:", ex);
//                    //todo 告警
//                    CoeusMonitor.addException(ex);
                }
            }
        }
    }

    /**
     * 添加任务
     * @param taskConfigVo
     * @throws SchedulerException
     */
    public void addTask(TaskConfigVo taskConfigVo) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(WacQuartzJob.class)
            .withIdentity(Constants.JOB_PREFIX + taskConfigVo.getId(), Constants.DEFAULT_JOB_GROUP)
            .build();
        jobDetail.getJobDataMap().put("taskName", taskConfigVo.getTask_name());
        jobDetail.getJobDataMap().put("taskId", taskConfigVo.getId());
        jobDetail.getJobDataMap().put("execType", Constants.EXEC_TYPE_AUTO);
        ScheduleExecute.taskConfigMap.put(String.valueOf(taskConfigVo.getId()), taskConfigVo);

        scheduleJob(jobDetail, createTrigger(taskConfigVo.getId(), taskConfigVo.getCron_exp()));
    }

    private void scheduleJob(final JobDetail jobDetail, final CronTrigger trigger)
            throws SchedulerException {
        if (!scheduler.checkExists(jobDetail.getKey())) {
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("================= scheduleJob key:{}, cron:{}", jobDetail.getKey()
                .getName(), trigger.getCronExpression());
        }
    }

    private CronTrigger createTrigger(final Integer taskId, final String cronExpression) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        return TriggerBuilder.newTrigger()
            .withIdentity(Constants.TRIGGER_PREFIX + taskId, Constants.DEFAULT_TRIGGER_GROUP)
            .withSchedule(cronScheduleBuilder).build();
    }

    /**
     * 更新Trigger
     * @param taskId
     * @param cronExpression
     * @throws SchedulerException
     */
    public void reScheduleJob(final Integer taskId, final String cronExpression)
            throws SchedulerException {
        scheduler.rescheduleJob(new TriggerKey(Constants.TRIGGER_PREFIX + taskId,
            Constants.DEFAULT_TRIGGER_GROUP), createTrigger(taskId, cronExpression));
        logger.info("================= rescheduleJob taskId:{}, newTrigger:{}", taskId,
                cronExpression);
    }

    /**
     * 入库
     * @param taskConfigModel
     */
    private void insertTaskConfigModel(TaskConfigModel taskConfigModel) throws Exception {
        taskConfigMapper.insertTaskConfig(taskConfigModel);
    }

    /**
     * 移除任务
     * @param taskId
     */
    public void removeTask(Integer taskId) throws SchedulerException {
        try {
            scheduler.deleteJob(new JobKey(Constants.JOB_PREFIX + taskId,
                    Constants.DEFAULT_JOB_GROUP));
            logger.info("task {} deleted", taskId);
        } catch (SchedulerException e) {
            logger.error("****************** remove task failed, error message is:", taskId, e);
            throw e;
        }
    }

    /**
     * 暂停调度中的job任务
     */
    public void pauseJob(Integer taskId) throws SchedulerException {
        try {
            scheduler.pauseJob(new JobKey(Constants.JOB_PREFIX + taskId,
                    Constants.DEFAULT_JOB_GROUP));
            logger.info("task {} pause", taskId);
        } catch (SchedulerException e) {
            logger.error("****************** pause Job {} failed, error message is:{}", taskId, e);
            throw e;
        }
    }

    /**
     * 暂停调度中所有的job任务
     */
    public void pauseAllJob() throws SchedulerException {
        try {
            scheduler.pauseAll();
            logger.info("pause all job.");
        } catch (SchedulerException e) {
            logger.error("****************** pause all job failed, error message is:", e);
            throw e;
        }
    }

    /**
     * 恢复所有停止的作业.
     */
    public void resumeAllPauseJob() throws SchedulerException {
        try {
            if (scheduler.isShutdown()) {
                return;
            }
            scheduler.resumeAll();
            logger.info("resume all pause job.");
        } catch (SchedulerException e) {
            logger.error("****************** resume all job failed, error message is:", e);
            throw e;
        }
    }


    /**
     * 加载所有任务
     */
    private void loadAllTask() {
        List<TaskConfigVo> taskConfigVos = taskConfigMapper.loadAllUsableSchedConfig();
        int errorCount = 0;
        DetectHost detectHost = DetectHost.getDetectHost();
        for (TaskConfigVo taskConfigVo : taskConfigVos) {
            try {
                addTask(taskConfigVo);
                detectHost.getTaskMap().put(String.valueOf(taskConfigVo.getId()), new DetectBean(taskConfigVo.getTask_group(), 1));
            } catch (Exception e) {
                logger.error("****************** add task failed, error message is:", e);
                errorCount++;
//                //todo 告警
//                CoeusMonitor.addException(e);
            }
        }

        logger.info("================= load {} task, error {} task.", taskConfigVos.size(),
            errorCount);
    }

    /**
     * 加载所有禁用、指定调度机主机
     */
    private void loadAllHosts() {
        List<HostVo> hostVos = taskHostMapper.loadHosts();
        List<String> disableHosts = null;
        List<String> ownerHosts = null;
        HostsUtil hostsUtil = HostsUtil.getHostsUtil();
        hostsUtil.clearHosts();

        for (HostVo hostVo : hostVos) {
            if (Constants.HOST_OWNER_YES == hostVo.getOwner().intValue()) {
                ownerHosts = hostsUtil.getOwnerHosts().get(String.valueOf(hostVo.getTask_config_id()));
                if (ownerHosts == null){
                    ownerHosts = new CopyOnWriteArrayList<>();
                    hostsUtil.getOwnerHosts().put(String.valueOf(hostVo.getTask_config_id()), ownerHosts);
                }
                ownerHosts.add(hostVo.getIp());
            } else if (Constants.COMMON_YES == hostVo.getDisabled().intValue()) {
                disableHosts = hostsUtil.getDisableHosts().get(
                    String.valueOf(hostVo.getTask_config_id()));
                if (disableHosts == null) {
                    disableHosts = new CopyOnWriteArrayList<>();
                    hostsUtil.getDisableHosts().put(String.valueOf(hostVo.getTask_config_id()), disableHosts);
                }
                disableHosts.add(hostVo.getIp());
            }
        }
    }

    /**
     * 启动task
     *
     * @throws SchedulerException
     */
    public void start() throws SchedulerException {
        loadAllHosts();
        loadAllTask();
        if (!isStarted()) {
            scheduler.start();
        } else {
            logger.info("scheduler has already started!");
        }
    }

    public boolean isStarted() throws SchedulerException {
        return scheduler.isStarted();
    }

}
