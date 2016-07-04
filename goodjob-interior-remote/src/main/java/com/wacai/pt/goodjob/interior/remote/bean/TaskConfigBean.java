package com.wacai.pt.goodjob.interior.remote.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuanwu on 16/3/25.
 */
public class TaskConfigBean implements Serializable {
    private static final long serialVersionUID = -2248295754490249398L;

    private Integer           id;

    /**
     * 是否修改立刻生效
     */
    private boolean           isEffect         = false;

    /**
     * 任务名称
     */
    private String            task_name;

    /**
     * 任务描述
     */
    private String            task_desc;

    /**
     * 业务code，必须唯一，对应dubbo group
     */
    private String            task_group;

    /**
     * 项目ID
     */
    private Integer           project_id;

    /**
     * 是否跳过， 0:不跳过， 1:跳过
     * 注：如果设置跳过，上次没执行完就不再执行
     */
    private Integer           delay_skip;

    /**
     * 精确执行 0:是，1:否
     * 0表示上次没执行完就不执行
     * 1表示上次没执行完，如果超过超时（timeout）时间还会执行
     */
    private Integer           exact_once;

    /**
     * 重试
     *
     * *暂时弃用
     */
    private Integer           retry;

    /**
     * 超时(秒)
     *
     */
    private Integer           timeout;

    /**
     * cron表达式
     */
    private String            cron_exp;

    /**
     * 任务类型
     * 0:默认, 1:分片, 2:任务依赖
     */
    private Integer           type;

    /**
     * 任务依赖关系
     */
    private String            job_data;
    /**
     * 分片参数
     */
    private List<String>      paramList;

    private String            lastModifyBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isEffect() {
        return isEffect;
    }

    public void setIsEffect(boolean isEffect) {
        this.isEffect = isEffect;
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

    @Deprecated
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

    public List<String> getParamList() {
        return paramList;
    }

    public void setParamList(List<String> paramList) {
        this.paramList = paramList;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
