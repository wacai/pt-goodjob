package com.wacai.pt.goodjob.schedule.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: tianwen
 */
public class TaskExecute implements Serializable {
    public enum State {
        PROCESSING(2, "处理中"), FAIL(1, "处理失败"), SUCCESS(0, "处理成功");

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

    private String percent;

    private Integer execType;

    private Integer taskConfigType;

    private Integer taskConfigId;

    private Date createTime;

    private Date lastUpdateTime;

    private Long startTime;

    private Long endTime;

    private Long schedTime;

    private int state;

    private String execMsg;

    private String taskName;

    private String execDesc;

    private String jobData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getJobData() {
        return jobData;
    }

    public void setJobData(String jobData) {
        this.jobData = jobData;
    }

    public Integer getTaskConfigId() {
        return taskConfigId;
    }

    public void setTaskConfigId(Integer taskConfigId) {
        this.taskConfigId = taskConfigId;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public Integer getTaskConfigType() {
        return taskConfigType;
    }

    public void setTaskConfigType(Integer taskConfigType) {
        this.taskConfigType = taskConfigType;
    }

    public String getExecDesc() {
        return execDesc;
    }

    public void setExecDesc(String execDesc) {
        this.execDesc = execDesc;
    }

    public Integer getExecType() {
        return execType;
    }

    public void setExecType(Integer execType) {
        this.execType = execType;
    }

    public Long getSchedTime() {
        return schedTime;
    }

    public void setSchedTime(Long schedTime) {
        this.schedTime = schedTime;
    }
}
