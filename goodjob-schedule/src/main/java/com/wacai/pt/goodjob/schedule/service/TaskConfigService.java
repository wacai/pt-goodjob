package com.wacai.pt.goodjob.schedule.service;

import com.wacai.pt.goodjob.schedule.vo.TaskConfigVo;

/**
 * Created by xuanwu on 16/5/24.
 */
public interface TaskConfigService {
    void updateTaskSchedTime(final Integer taskId, final Long next_fire_time,
                             final Long prev_fire_time);

    /**
     * 查询调度配置
     * @param id
     * @return
     */
    TaskConfigVo getSchedConfigById(Integer id);
}
