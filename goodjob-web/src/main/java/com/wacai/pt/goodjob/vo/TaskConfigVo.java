package com.wacai.pt.goodjob.vo;

import com.wacai.pt.goodjob.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by xuanwu on 16/3/25.
 */
public class TaskConfigVo {

    private Integer id;

    /**
     * 任务名称
     */
    private String  task_name;

    /**
     * 任务状态
     * 0:停用，1:启用
     */
    private Integer task_state;

    /**
     * 任务描述
     */
    private String  task_desc;

    /**
     * 业务code，必须唯一，对应dubbo group
     */
    private String  task_group;

    /**
     * 项目ID
     */
    private Integer project_id;

    /**
     * 项目名
     */
    private String  project_name;

    /**
     * cron表达式
     */
    private String  cron_exp;

    /**
     * 任务类型
     * 0:默认, 1:分片, 2:任务依赖
     */
    private Integer type;

    /**
     * 延时是否跳过， 0:不跳过， 1:跳过
     * 注：如果设置跳过，上次没执行完就不再执行
     */
    private Integer delay_skip;

    /**
     * 精确执行 0:是，1:否
     * 0表示上次没执行完就不执行
     * 1表示上次没执行完，如果超过超时（timeout）时间还会执行
     */
    private Integer exact_once;

    /**
     * 重试次数(暂时不用)
     */
    private Integer retry;

    /**
     * 超时(单位秒)
     */
    private Integer timeout;

    private String  next_fire_time;

    private String  prev_fire_time;

    private String  lastModifyBy;

    private Date    lastUpdateTime;

    private Date    createTime;

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

    public Integer getExact_once() {
        return exact_once;
    }

    public void setExact_once(Integer exact_once) {
        this.exact_once = exact_once;
    }

    public Integer getTask_state() {
        return task_state;
    }

    public void setTask_state(Integer task_state) {
        this.task_state = task_state;
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

    public Integer getDelay_skip() {
        return delay_skip;
    }

    public void setDelay_skip(Integer delay_skip) {
        this.delay_skip = delay_skip;
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

    public String getNext_fire_time() {
        return next_fire_time;
    }

    public void setNext_fire_time(Long next_fire_time) {
        if (next_fire_time != null) {
            this.next_fire_time = DateUtils.format(new Date(next_fire_time), DateUtils.YMD_HMS);
        }
    }

    public String getPrev_fire_time() {
        return prev_fire_time;
    }

    public void setPrev_fire_time(Long prev_fire_time) {
        if (prev_fire_time != null) {
            this.prev_fire_time = DateUtils.format(new Date(prev_fire_time), DateUtils.YMD_HMS);
        }
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

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
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
}
