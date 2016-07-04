package com.wacai.pt.goodjob.schedule.model;

import com.wacai.pt.goodjob.interior.remote.bean.TaskParamBean;

import java.util.Date;

/**
 * Created by tianwen */
public class TaskParamModel {
    private Integer id;
    private String  param;
    private Integer taskConfigId = 0;
    private Date    lastUpdateTime;
    private Date    createTime;
    private String  lastModifyBy;

    public TaskParamModel() {

    }

    public TaskParamModel(TaskParamBean taskParamBean) {
        this.param = taskParamBean.getParam();
        this.lastModifyBy = taskParamBean.getLastModifyBy();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Integer getTaskConfigId() {
        return taskConfigId;
    }

    public void setTaskConfigId(Integer taskConfigId) {
        this.taskConfigId = taskConfigId;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    @Override
    public String toString() {
        return "TaskParamModel{" + "id=" + id + ", param='" + param + '\'' + ", taskConfigId="
               + taskConfigId + ", lastUpdateTime=" + lastUpdateTime + ", createTime=" + createTime
               + ", lastModifyBy='" + lastModifyBy + '\'' + '}';
    }
}
