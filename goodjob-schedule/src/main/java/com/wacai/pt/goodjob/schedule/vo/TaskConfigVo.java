package com.wacai.pt.goodjob.schedule.vo;

import java.util.List;

/**
 * Created by xuanwu on 16/4/10.
 */
public class TaskConfigVo {
    private Integer id;
    private String  task_name;
    private String  task_group;
    private String  cron_exp;
    private Integer delay_skip;
    private Integer exact_once;
    private Integer retry;
    private Integer timeout;
    private Integer type;
    private String  job_data;
    private List<TaskParamVo> taskParamVos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_group() {
        return task_group;
    }

    public void setTask_group(String task_group) {
        this.task_group = task_group;
    }

    public String getCron_exp() {
        return cron_exp;
    }

    public void setCron_exp(String cron_exp) {
        this.cron_exp = cron_exp;
    }

    public Integer getDelay_skip() {
        return delay_skip;
    }

    public void setDelay_skip(Integer delay_skip) {
        this.delay_skip = delay_skip;
    }

    public Integer getExact_once() {
        return exact_once;
    }

    public void setExact_once(Integer exact_once) {
        this.exact_once = exact_once;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getJob_data() {
        return job_data;
    }

    public void setJob_data(String job_data) {
        this.job_data = job_data;
    }

    public List<TaskParamVo> getTaskParamVos() {
        return taskParamVos;
    }

    public void setTaskParamVos(List<TaskParamVo> taskParamVos) {
        this.taskParamVos = taskParamVos;
    }
}
