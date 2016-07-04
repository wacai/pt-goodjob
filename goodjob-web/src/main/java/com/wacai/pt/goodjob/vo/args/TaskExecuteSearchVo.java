package com.wacai.pt.goodjob.vo.args;

import com.wacai.pt.goodjob.common.utils.DateUtils;
import com.wacai.pt.goodjob.common.utils.StringUtils;

import java.util.Date;

/**
 * Created by xuanwu on 16/4/1.
 */
public class TaskExecuteSearchVo {
    private Integer task_config_id;
    private Integer state;
    private Date startTime;
    private Date endTime;

    public Integer getTask_config_id() {
        return task_config_id;
    }

    public void setTask_config_id(Integer task_config_id) {
        this.task_config_id = task_config_id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        if (state != null && state >= 0) {
            this.state = state;
        }
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        if (!StringUtils.isNullOrBlank(startTime)) {
            this.startTime = DateUtils.parse(startTime, DateUtils.YMD_DASH_WITH_TIME);
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        if (!StringUtils.isNullOrBlank(endTime)) {
            this.endTime = DateUtils.parse(endTime, DateUtils.YMD_DASH_WITH_TIME);
        }
    }
}
