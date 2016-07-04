package com.wacai.pt.goodjob.vo;

import com.wacai.pt.goodjob.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by xuanwu on 16/3/29.
 */
public class TaskExecuteVo {
    private Long    id;
    private Integer task_config_id;
    /**
     * 0:默认, 1:分片, 2:任务依赖
     */
    private Integer task_config_type;
    /**
     * 0:默认, 1:手动
     */
    private Integer exec_type;
    private String  task_name;
    private String  exec_desc;
    private String  exec_msg;
    private String  start_time;
    private String  end_time;
    private String  sched_time;
    private String  percent;
    /**
     * 0:成功, 1:失败, 2:处理中, 3:超时
     */
    private Integer state;
    private String  lastUpdateTime;
    private String  createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTask_config_id() {
        return task_config_id;
    }

    public void setTask_config_id(Integer task_config_id) {
        this.task_config_id = task_config_id;
    }

    public Integer getTask_config_type() {
        return task_config_type;
    }

    public void setTask_config_type(Integer task_config_type) {
        this.task_config_type = task_config_type;
    }

    public Integer getExec_type() {
        return exec_type;
    }

    public void setExec_type(Integer exec_type) {
        this.exec_type = exec_type;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getExec_desc() {
        return exec_desc;
    }

    public void setExec_desc(String exec_desc) {
        this.exec_desc = exec_desc;
    }

    public String getExec_msg() {
        return exec_msg;
    }

    public void setExec_msg(String exec_msg) {
        this.exec_msg = exec_msg;
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

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getSched_time() {
        return sched_time;
    }

    public void setSched_time(Long sched_time) {
        this.sched_time = DateUtils.format(new Date(sched_time), DateUtils.YMD_HMS);
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
