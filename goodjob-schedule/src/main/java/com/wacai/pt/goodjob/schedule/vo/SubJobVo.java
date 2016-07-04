package com.wacai.pt.goodjob.schedule.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xuanwu on 16/5/24.
 */
public class SubJobVo implements Serializable {

    public String getJobKey() {
        return jobName + jobGroup;
    }

    public static final State[] stateArr = State.values();

    public enum State {
        REDAY(5, "准备好"), RETRY(4, "重试中"), TIMEOUT(3, "超时"), PROCESSING(2, "处理中"), FAIL(1, "处理失败"), SUCCESS(
                0, "处理成功");

        private int value;
        private String description;

        private State(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

    }

    private Long id;

    private Long taskExecuteId;

    private String jobGroup;

    private String jobName;

    private String hostIp;

    private String jobParam;

    private Long startTime;

    private Long endTime;

    private Long jobOffset;

    private int state;

    private Date createTime;

    private Date lastUpdateTime;

    private List<TaskDyVo> taskDyVoList;

    private String execMsg;

    private Integer retryCount;

    private Long retryStartTime;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SubJobVo job = (SubJobVo) o;

        if (!id.equals(job.id))
            return false;
        if (!taskExecuteId.equals(job.taskExecuteId))
            return false;
        if (!jobGroup.equals(job.jobGroup))
            return false;
        return jobName.equals(job.jobName);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + taskExecuteId.hashCode();
        result = 31 * result + jobGroup.hashCode();
        result = 31 * result + jobName.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskExecuteId() {
        return taskExecuteId;
    }

    public void setTaskExecuteId(Long taskExecuteId) {
        this.taskExecuteId = taskExecuteId;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getJobOffset() {
        return jobOffset;
    }

    public void setJobOffset(Long jobOffset) {
        this.jobOffset = jobOffset;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<TaskDyVo> getTaskDyVoList() {
        return taskDyVoList;
    }

    public void setTaskDyVoList(List<TaskDyVo> taskDyVoList) {
        this.taskDyVoList = taskDyVoList;
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Long getRetryStartTime() {
        return retryStartTime;
    }

    public void setRetryStartTime(Long retryStartTime) {
        this.retryStartTime = retryStartTime;
    }

    public String getJobParam() {
        return jobParam;
    }

    public void setJobParam(String jobParam) {
        this.jobParam = jobParam;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    @Override
    public String toString() {
        return "SubJobVo{" +
                "id=" + id +
                ", taskExecuteId=" + taskExecuteId +
                ", jobGroup='" + jobGroup + '\'' +
                ", jobName='" + jobName + '\'' +
                ", hostIp='" + hostIp + '\'' +
                ", jobParam='" + jobParam + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", jobOffset=" + jobOffset +
                ", state=" + state +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", taskDyVoList=" + taskDyVoList +
                ", errorMsg='" + execMsg + '\'' +
                ", retryCount=" + retryCount +
                ", retryStartTime=" + retryStartTime +
                '}';
    }
}
