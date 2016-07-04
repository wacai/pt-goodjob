package com.wacai.pt.goodjob.schedule.model;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.interior.remote.bean.TaskConfigBean;

import java.util.Date;

/**
 * Created by xuanwu on 16/3/23.
 */
public class TaskConfigModel {
    private Integer id;
    private String  task_name;
    private Integer task_state = 0;
    private Integer deleted    = Constants.COMMON_NO;
    private String  task_desc;
    private String  task_group;
    private Integer project_id;
    private Long    next_fire_time;
    private Long    prev_fire_time;
    private Integer delay_skip;
    private Integer exact_once;
    private Integer retry;
    private Integer timeout;
    private String  cron_exp;
    private Integer type       = Constants.TASK_CONFIG_TYPE_0;
    private String  job_data;
    private Date    lastUpdateTime;
    private Date    createTime;
    private String  lastModifyBy;

    public TaskConfigModel() {

    }

    public TaskConfigModel(TaskConfigBean taskConfigBean) {
        this.task_name = taskConfigBean.getTask_name();
        this.task_desc = taskConfigBean.getTask_desc();
        this.task_group = taskConfigBean.getTask_group();
        this.project_id = taskConfigBean.getProject_id();
        this.delay_skip = taskConfigBean.getDelay_skip();
        this.exact_once = taskConfigBean.getExact_once();
        this.retry = taskConfigBean.getRetry();
        this.timeout = taskConfigBean.getTimeout();
        this.cron_exp = taskConfigBean.getCron_exp();
        this.type = taskConfigBean.getType();
        this.job_data = taskConfigBean.getJob_data();
        this.lastModifyBy = taskConfigBean.getLastModifyBy();
    }

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

    public Integer getTask_state() {
        return task_state;
    }

    public void setTask_state(Integer task_state) {
        this.task_state = task_state;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public String getTask_group() {
        return task_group;
    }

    public void setTask_group(String task_group) {
        this.task_group = task_group;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public Long getNext_fire_time() {
        return next_fire_time;
    }

    public void setNext_fire_time(Long next_fire_time) {
        this.next_fire_time = next_fire_time;
    }

    public Long getPrev_fire_time() {
        return prev_fire_time;
    }

    public void setPrev_fire_time(Long prev_fire_time) {
        this.prev_fire_time = prev_fire_time;
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

    public String getCron_exp() {
        return cron_exp;
    }

    public void setCron_exp(String cron_exp) {
        this.cron_exp = cron_exp;
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
        return "TaskConfigModel{" +
                "id=" + id +
                ", task_name='" + task_name + '\'' +
                ", task_state=" + task_state +
                ", deleted=" + deleted +
                ", task_desc='" + task_desc + '\'' +
                ", task_group='" + task_group + '\'' +
                ", project_id=" + project_id +
                ", next_fire_time=" + next_fire_time +
                ", prev_fire_time=" + prev_fire_time +
                ", delay_skip=" + delay_skip +
                ", exact_once=" + exact_once +
                ", retry=" + retry +
                ", timeout=" + timeout +
                ", cron_exp='" + cron_exp + '\'' +
                ", type=" + type +
                ", job_data='" + job_data + '\'' +
                ", lastUpdateTime=" + lastUpdateTime +
                ", createTime=" + createTime +
                ", lastModifyBy='" + lastModifyBy + '\'' +
                '}';
    }
}
