package com.wacai.pt.goodjob.schedule.service.impl;

import com.wacai.pt.goodjob.schedule.mapper.TaskConfigMapper;
import com.wacai.pt.goodjob.schedule.model.TaskConfigModel;
import com.wacai.pt.goodjob.schedule.service.TaskConfigService;
import com.wacai.pt.goodjob.schedule.util.ThreadUtil;
import com.wacai.pt.goodjob.schedule.vo.TaskConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xuanwu on 16/5/24.
 */
@Component("taskConfigService")
public class TaskConfigServiceImpl implements TaskConfigService {
    @Autowired
    private TaskConfigMapper taskConfigMapper;

    public void updateTaskSchedTime(final Integer taskId, final Long next_fire_time,
                                    final Long prev_fire_time) {
        try {
            ThreadUtil.ignoreThreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    TaskConfigModel taskConfigModel = new TaskConfigModel();
                    taskConfigModel.setId(taskId);
                    taskConfigModel.setPrev_fire_time(prev_fire_time);
                    taskConfigModel.setNext_fire_time(next_fire_time);

                    taskConfigMapper.updateTaskConfigModelById(taskConfigModel);
                }
            });
        } catch (Throwable e) {
            //忽略
        }
    }

    /**
     * 查询调度配置
     * @param id
     * @return
     */
    public TaskConfigVo getSchedConfigById(Integer id){
        return taskConfigMapper.getSchedConfigById(id);
    }
}
