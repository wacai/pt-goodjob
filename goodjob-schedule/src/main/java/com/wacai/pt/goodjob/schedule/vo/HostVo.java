package com.wacai.pt.goodjob.schedule.vo;

/**
 * Created by xuanwu on 16/5/11.
 */
public class HostVo {
    private Integer id;
    private Integer task_config_id;
    private String  ip;
    //0:不可用, 1:可用
    private Integer disabled;
    //0:否, 1:是
    private Integer owner;

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

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }
}
