package com.wacai.pt.goodjob.schedule.detect;

/**
 * Created by xuanwu on 16/6/8.
 */
public class DetectBean {
    private String  taskGroup;
    private Integer task_state;
    private long    createTime = System.currentTimeMillis();

    public DetectBean() {

    }

    public DetectBean(String taskGroup, Integer task_state) {
        this.taskGroup = taskGroup;
        this.task_state = task_state;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public Integer getTask_state() {
        return task_state;
    }

    public void setTask_state(Integer task_state) {
        this.task_state = task_state;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
