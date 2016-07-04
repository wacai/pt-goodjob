package com.wacai.pt.goodjob.vo.args;

/**
 * Created by xuanwu on 16/3/29.
 */
public class TaskConfigSearchVo {

    /**
     * 任务名称
     * 模糊
     */
    private String task_name;

    /**
     * 业务code，必须唯一，对应dubbo group
     * 模糊
     */
    private String task_group;

    /**
     * 项目id
     */
    private Integer project_id;

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

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        if(project_id != null && project_id != -1) {
            this.project_id = project_id;
        }
    }
}
