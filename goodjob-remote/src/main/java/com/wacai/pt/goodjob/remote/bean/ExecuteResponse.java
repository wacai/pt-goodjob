package com.wacai.pt.goodjob.remote.bean;

import java.io.Serializable;

/**
 * Author: tianwen
 */
public class ExecuteResponse implements Serializable {
    private static final long serialVersionUID = -178286522317862592L;
    private Long              jobId;

    private Integer           taskId;

    private Long              taskExecuteId;

    private String            hostIp;

    private Integer           state;

    private Integer           jobCount;

    private Integer           taskType;


    private String            execMsg;

    private Long              elapsed;

    private Long              endTime;

    private Long              responseTime;

    public ExecuteResponse(){

    }

    public ExecuteResponse(ExecuteRequest executeRequest){
        this.taskId = executeRequest.getTaskId();
        this.taskExecuteId = executeRequest.getTaskExecuteId();
        this.jobId = executeRequest.getJobId();
        this.taskType = executeRequest.getTaskType();
        this.jobCount = executeRequest.getJobCount();
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public Long getTaskExecuteId() {
        return taskExecuteId;
    }

    public void setTaskExecuteId(Long taskExecuteId) {
        this.taskExecuteId = taskExecuteId;
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        String _execMsg = execMsg;
        if (_execMsg != null && _execMsg.length() > 4000){
            _execMsg = _execMsg.substring(0, 4000);
        }
        this.execMsg = _execMsg;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public Long getElapsed() {
        return elapsed;
    }

    public void setElapsed(Long elapsed) {
        this.elapsed = elapsed;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getJobCount() {
        return jobCount;
    }

    public void setJobCount(Integer jobCount) {
        this.jobCount = jobCount;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }
}
