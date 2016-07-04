package com.wacai.pt.goodjob.schedule.intern;

import com.alibaba.dubbo.remoting.ExecutionException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcContext;
import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.remote.bean.ExecuteRequest;
import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.remote.service.JobExecuteRespService;
import com.wacai.pt.goodjob.remote.service.JobExecuteService;
import com.wacai.pt.goodjob.schedule.adaptor.SubJobExecuteAdaptor;
import com.wacai.pt.goodjob.schedule.model.SubJobModel;
import com.wacai.pt.goodjob.schedule.model.TaskExecuteModel;
import com.wacai.pt.goodjob.schedule.service.SubJobService;
import com.wacai.pt.goodjob.schedule.service.TaskConfigService;
import com.wacai.pt.goodjob.schedule.service.TaskExecuteService;
import com.wacai.pt.goodjob.schedule.vo.LastTaskExecuteVo;
import com.wacai.pt.goodjob.schedule.vo.TaskConfigVo;
import com.wacai.pt.goodjob.schedule.vo.TaskParamVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuanwu on 16/4/10.
 */
@Component("scheduleExecute")
public class ScheduleExecute {
    private static final Logger             logger        = LoggerFactory
                                                              .getLogger(ScheduleExecute.class);
    private static JsonMapper               jsonMapper    = JsonMapper.getNonNullBinder();
    //调度配置
    //key: taskId
    public static Map<String, TaskConfigVo> taskConfigMap = new ConcurrentHashMap<>(256);
    @Autowired
    private TaskConfigService               taskConfigService;

    @Autowired
    private TaskExecuteService              taskExecuteService;

    @Autowired
    private SubJobService                   subJobService;

    @Autowired
    private JobExecuteRespService           jobExecuteRespService;

    private TaskConfigVo getTaskConfigVo(Integer taskId) {
        TaskConfigVo taskConfigVo = taskConfigMap.get(String.valueOf(taskId));
        if (taskConfigVo == null) {
            taskConfigVo = taskConfigService.getSchedConfigById(taskId);
            if (taskConfigVo != null) {
                taskConfigMap.put(String.valueOf(taskId), taskConfigVo);
            }
        }

        return taskConfigVo;
    }

    public void rpcExecute(Map<String, Object> contextMap) throws Throwable {
        Integer taskId = (Integer) contextMap.get("taskId");
        Integer execType = (Integer) contextMap.get("execType");
        Long schedTime = (Long) contextMap.get("schedTime");
        TaskConfigVo taskConfigVo = getTaskConfigVo(taskId);
        if (taskConfigVo == null) {
            String errorMsg = "java.lang.NullPointerException:没有获取到任务配置，请检查数据, taskId:" + taskId;
//            //todo 告警
//            CoeusMonitor.addException(errorMsg);
            throw new Exception(errorMsg);
        }

        //判断是否跳过
        if (Constants.DELAY_SKIP_YES == taskConfigVo.getDelay_skip().intValue()) {
            LastTaskExecuteVo lastTaskExecuteVo = taskExecuteService
                .findLastTaskExecuteByTaskId(taskId);
            if (lastTaskExecuteVo != null) {
                int state = lastTaskExecuteVo.getState().intValue();
                if (Constants.EXEC_STATE_PROCESSING == state
                    || (Constants.EXEC_STATE_FAIL == state && lastTaskExecuteVo.getEnd_time() == null)) {
                    if (Constants.EXACT_ONCE_YES == state) {
                        logger.warn("----------任务跳过--------------- contextMap:{}",
                            jsonMapper.toJson(contextMap));
                        return;
                    } else {
                        if (System.currentTimeMillis() - lastTaskExecuteVo.getStart_time() < taskConfigVo
                            .getTimeout() * 1000L) {
                            logger.warn("----------任务跳过--------------- contextMap:{}",
                                jsonMapper.toJson(contextMap));
                            return;
                        }
                    }
                }
            }
        }

        //任务执行入库
        int jobCount = getJobCount(taskConfigVo);
        TaskExecuteModel taskExecuteModel = new TaskExecuteModel(taskId, jobCount,
            taskConfigVo.getType(), execType, taskConfigVo.getTask_name(), schedTime);
        taskExecuteService.insertTaskExecute(taskExecuteModel);
        contextMap.put("taskExecuteId", taskExecuteModel.getId());
        contextMap.put("jobCount", jobCount);

        //更新task执行时间
        if (execType.intValue() == Constants.EXEC_TYPE_AUTO) {
            taskConfigService.updateTaskSchedTime(taskId, (Long) contextMap.get("preFireTime"),
                (Long) contextMap.get("nextFireTime"));
        }

        //默认执行(单个job)
        if (Constants.TASK_CONFIG_TYPE_0 == taskConfigVo.getType().intValue()) {
            defaultExecute(taskConfigVo, contextMap);
        }

        //分片执行
        if (Constants.TASK_CONFIG_TYPE_1 == taskConfigVo.getType().intValue()) {
            distributedExecute(taskConfigVo, contextMap);
        }

        //依赖执行
        if (Constants.TASK_CONFIG_TYPE_2 == taskConfigVo.getType().intValue()) {
            dependExecute(taskConfigVo, contextMap);
        }
    }

    private int getJobCount(TaskConfigVo taskConfigVo) {
        int jobCount = 1;

        //分片执行
        if (Constants.TASK_CONFIG_TYPE_1 == taskConfigVo.getType().intValue()) {
            jobCount = taskConfigVo.getTaskParamVos().size();
        }

        //依赖执行
        if (Constants.TASK_CONFIG_TYPE_2 == taskConfigVo.getType().intValue()) {

        }

        return jobCount;
    }

    /**
     * 默认执行(单个job)
     * @param taskConfigVo
     */
    private void defaultExecute(TaskConfigVo taskConfigVo, Map<String, Object> contextMap)
            throws Throwable {
        ExecuteRequest executeRequest = new ExecuteRequest();
        executeRequest.setTaskId(taskConfigVo.getId());
        executeRequest.setTaskExecuteId((Long) contextMap.get("taskExecuteId"));
        executeRequest.setTaskGroup(taskConfigVo.getTask_group());
        executeRequest.setTaskType(taskConfigVo.getType());
        executeRequest.setJobCount(1);
        fireJob(executeRequest);
    }

    /**
     * 分片执行
     * @param taskConfigVo
     */
    private void distributedExecute(TaskConfigVo taskConfigVo, Map<String, Object> contextMap)
            throws Throwable {
        ExecuteRequest executeRequest = new ExecuteRequest();
        executeRequest.setTaskId(taskConfigVo.getId());
        executeRequest.setTaskExecuteId((Long) contextMap.get("taskExecuteId"));
        executeRequest.setTaskGroup(taskConfigVo.getTask_group());
        executeRequest.setTaskType(taskConfigVo.getType());
        executeRequest.setJobCount((Integer) contextMap.get("jobCount"));
        for (TaskParamVo taskParamVo : taskConfigVo.getTaskParamVos()) {
            executeRequest.setParam(taskParamVo.getParam());
            fireJob(executeRequest);
        }
    }

    /**
     * 依赖执行
     * @param taskConfigVo
     */
    private void dependExecute(TaskConfigVo taskConfigVo, Map<String, Object> contextMap)
            throws Throwable {

    }

    /**
     *  远程执行job
     * @param executeRequest
     * @throws Throwable
     */
    public void fireJob(ExecuteRequest executeRequest) throws Throwable {
        JobExecuteService jobExecuteService = SubJobExecuteAdaptor
            .getJobExecuteService(executeRequest.getTaskGroup());
        try {
            if (!executeRequest.getRetry()) {
                if (Constants.TASK_CONFIG_TYPE_2 != executeRequest.getTaskType().intValue()) {
                    SubJobModel subJobModel = new SubJobModel(executeRequest);
                    subJobModel.setState(Constants.EXEC_STATE_PROCESSING);
                    subJobService.insertSubJob(subJobModel);
                    executeRequest.setJobId(subJobModel.getId());
                } else {
                    //任务依赖，更新state为处理中
                }
            }

            //远程调用
            jobExecuteService.execute(executeRequest);

            //更新hostIp
            subJobService.updateHostIpById(RpcContext.getContext().getRemoteHost(),
                executeRequest.getJobId());
        } catch (ExecutionException ee) {
            logger.warn("ExecutionException:", ee);
//            //todo 告警
//            CoeusMonitor.addException("Job execute error", ee, jsonMapper.toJson(executeRequest));
        } catch (TimeoutException te) {
            logger.warn("TimeoutException:", te);
//            //todo 告警
//            CoeusMonitor.addException("Job execute error", te, jsonMapper.toJson(executeRequest));
        } catch (Throwable t) {
            logger.warn("Throwable:{}, executeRequest:{}", t, jsonMapper.toJson(executeRequest));
            ExecuteResponse executeResponse = new ExecuteResponse(executeRequest);
            executeResponse.setEndTime(System.currentTimeMillis());
            String execMsg = t.getMessage();
            if (execMsg == null) {
                execMsg = t.toString();
            }
            executeResponse.setExecMsg(execMsg);
            executeResponse.setState(Constants.EXEC_STATE_FAIL);
            executeResponse.setHostIp(RpcContext.getContext().getRemoteHost());

            jobExecuteRespService.executeResp(executeResponse);
            throw t;
        }
    }

}
