package com.wacai.pt.goodjob.remote.bean;

import java.io.Serializable;

/**
 * Author: tianwen
 */
public class ExecuteRequest implements Serializable {
    private static final long serialVersionUID = -8277029044513763284L;
    private Integer           taskId;

    private Long              taskExecuteId;

    private Integer           taskType;

    private Integer           jobCount;

    private String            execMsg;

    private String            taskGroup;

    private Long              jobId;

    private boolean           retry            = false;

    private String            param;

    public ExecuteRequest() {

    }

    public Long getTaskExecuteId() {
        return taskExecuteId;
    }

    public void setTaskExecuteId(Long taskExecuteId) {
        this.taskExecuteId = taskExecuteId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getJobCount() {
        return jobCount;
    }

    public void setJobCount(Integer jobCount) {
        this.jobCount = jobCount;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public boolean getRetry() {
        return retry;
    }

    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "ExecuteRequest{" +
                "taskId=" + taskId +
                ", taskExecuteId=" + taskExecuteId +
                ", taskType=" + taskType +
                ", jobCount=" + jobCount +
                ", execMsg='" + execMsg + '\'' +
                ", taskGroup='" + taskGroup + '\'' +
                ", jobId=" + jobId +
                ", retry=" + retry +
                ", param='" + param + '\'' +
                '}';
    }
}
