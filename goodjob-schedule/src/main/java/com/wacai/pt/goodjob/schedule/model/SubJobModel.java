package com.wacai.pt.goodjob.schedule.model;

import com.wacai.pt.goodjob.remote.bean.ExecuteRequest;

import java.util.Date;

/**
 * Created by xuanwu on 16/4/10.
 */
public class SubJobModel {
    private Long    id;
    private Long    task_exec_id;
    private String  host_ip;
    private String  job_name;
    private String  job_group;
    private String  job_param;
    private Long    start_time = System.currentTimeMillis();
    private Long    end_time;

    private Long    job_offset;
    private Integer retry_count = 0;
    private Long    retry_start_time;
    /**
     * 0:成功, 1:失败, 2:处理中, 3:超时
     */
    private Integer state;
    private String  exec_msg;
    private Date    createTime;
    private Date    lastUpdateTime;

    public SubJobModel(){

    }

    public SubJobModel(ExecuteRequest executeRequest){
        this.task_exec_id = executeRequest.getTaskExecuteId();
        this.job_group = executeRequest.getTaskGroup();
        this.job_param = executeRequest.getParam();
    }


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

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public Long getJob_offset() {
        return job_offset;
    }

    public void setJob_offset(Long job_offset) {
        this.job_offset = job_offset;
    }

    public Integer getRetry_count() {
        return retry_count;
    }

    public void setRetry_count(Integer retry_count) {
        this.retry_count = retry_count;
    }

    public Long getRetry_start_time() {
        return retry_start_time;
    }

    public void setRetry_start_time(Long retry_start_time) {
        this.retry_start_time = retry_start_time;
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
}
