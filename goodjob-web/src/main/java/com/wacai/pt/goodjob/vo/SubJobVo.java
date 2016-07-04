package com.wacai.pt.goodjob.vo;

import com.wacai.pt.goodjob.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by xuanwu on 16/3/29.
 */
public class SubJobVo {
    private Long    id;
    private Long    task_exec_id;
    private String  host_ip;
    private String  job_name;
    private String  job_group;
    private String  job_param;
    private String  start_time;
    private String  end_time;
    /**
     * 0:成功, 1:失败, 2:处理中, 3:超时
     */
    private Integer state;
    private String  exec_msg;
    private String  createTime;
    private String  lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTask_exec_id() {
        return task_exec_id;
    }

    public void setTask_exec_id(Long task_exec_id) {
        this.task_exec_id = task_exec_id;
    }

    public String getHost_ip() {
        return host_ip;
    }

    public void setHost_ip(String host_ip) {
        this.host_ip = host_ip;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_group() {
        return job_group;
    }

    public void setJob_group(String job_group) {
        this.job_group = job_group;
    }

    public String getJob_param() {
        return job_param;
    }

    public void setJob_param(String job_param) {
        this.job_param = job_param;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        if (start_time != null) {
            this.start_time = DateUtils.format(new Date(start_time), DateUtils.YMD_HMS);
        }
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        if (end_time != null) {
            this.end_time = DateUtils.format(new Date(end_time), DateUtils.YMD_HMS);
        }
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExec_msg() {
        return exec_msg;
    }

    public void setExec_msg(String exec_msg) {
        this.exec_msg = exec_msg;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = DateUtils.format(lastUpdateTime, DateUtils.YMD_HMS);
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = DateUtils.format(createTime, DateUtils.YMD_HMS);
    }
}
