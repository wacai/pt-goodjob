package com.wacai.pt.goodjob.schedule.mapper;

import com.wacai.pt.goodjob.interior.remote.bean.TaskConfigBean;
import com.wacai.pt.goodjob.interior.remote.bean.TaskHandleBean;
import com.wacai.pt.goodjob.schedule.model.TaskConfigModel;
import com.wacai.pt.goodjob.schedule.vo.TaskConfigVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuanwu on 16/3/23.
 */
@Component("taskConfigMapper")
public interface TaskConfigMapper {
    void insertTaskConfig(TaskConfigModel taskConfigModel);

    void updateTaskConfigById(TaskConfigBean taskConfigBean);

    void updateTaskConfigModelById(TaskConfigModel taskConfigModel);

    void updateCron_expById(TaskHandleBean taskHandleBean);

    List<TaskConfigModel> loadAllUsableTasks();

    /**
     * 查询所有可用的task
     * @return
     */
    List<TaskConfigVo> loadAllUsableSchedConfig();

    /**
     * 调度配置
     * @param id
     * @return
     */
    TaskConfigModel findTaskById(@Param("id") Integer id);

    /**
     * 查询调度配置（无task_state条件）
     * @param id
     * @return
     */
    TaskConfigVo findSchedConfigWithoutStateById(@Param("id") Integer id);

    /**
     * 查询调度配置
     * @param id
     * @return
     */
    TaskConfigVo getSchedConfigById(@Param("id") Integer id);

    TaskConfigModel getTaskConfigById(@Param("id") Integer id);

    void deleteTaskConfigById(TaskHandleBean taskHandleBean);

    void stopTaskConfigById(TaskHandleBean taskHandleBean);

    void startTaskConfigById(TaskHandleBean taskHandleBean);
}
