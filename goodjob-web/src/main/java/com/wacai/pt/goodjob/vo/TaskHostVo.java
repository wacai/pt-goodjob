package com.wacai.pt.goodjob.vo;

import java.util.Date;

/**
 * Created by xuanwu on 16/3/29.
 */
public class TaskHostVo {
    private Integer id;
    private Integer task_config_id;
    private String  ip;
    /**
     * 0:否, 1:是
     */
    private Integer owner;
    /**
     * 0:不可用, 1:可用
     */
    private Integer disabled;
    private String  host_desc;
    private Date    lastUpdateTime;
    private Date    createTime;
    private String  lastModifyBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTask_config_id() {
        return task_config_id;
    }

    public void setTask_config_id(Integer task_config_id) {
        this.task_config_id = task_config_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getHost_desc() {
        return host_desc;
    }

    public void setHost_desc(String host_desc) {
        this.host_desc = host_desc;
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
}
