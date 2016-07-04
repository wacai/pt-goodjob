package com.wacai.pt.goodjob.schedule.vo;

import java.io.Serializable;

/**
 * Created by xuanwu on 16/5/24.
 */
public class TaskDyVo implements Serializable{

    private static final long serialVersionUID = -931950067342955115L;
    private String jobName;

    private String jobGroup;

    public TaskDyVo(){

    }

    public TaskDyVo(String jobName, String jobGroup){
        this.jobGroup = jobGroup;
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (jobName == null || jobGroup == null){
            return false;
        }
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDyVo taskDyVo = (TaskDyVo) o;

        if (!jobName.equals(taskDyVo.jobName)) return false;
        return jobGroup.equals(taskDyVo.jobGroup);

    }

    @Override
    public int hashCode() {
        if (jobName == null || jobGroup == null){
            return 0;
        }
        int result = jobName.hashCode();
        result = 31 * result + jobGroup.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TaskDyVo{" +
                "jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                '}';
    }
}
