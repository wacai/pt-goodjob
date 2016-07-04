package com.wacai.pt.goodjob.interior.remote.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xuanwu on 16/4/5.
 */
public class TaskParamBean implements Serializable {
    private static final long serialVersionUID = -331739133466218409L;

    private Integer id;
    private String  param;
    private Integer task_config_id;
    private Date    lastUpdateTime;
    private String  lastModifyBy;

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

    public Integer getTask_config_id() {
        return task_config_id;
    }

    public void setTask_config_id(Integer task_config_id) {
        this.task_config_id = task_config_id;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
