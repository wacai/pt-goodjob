package com.wacai.pt.goodjob.interior.remote.bean;

import java.io.Serializable;

/**
 * Created by xuanwu on 16/4/5.
 */
public class HostHandleBean implements Serializable {
    private static final long serialVersionUID = -247761122655540004L;

    private Integer           id;
    private Integer           taskId;
    private String            lastModifyBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
