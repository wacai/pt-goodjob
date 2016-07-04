package com.wacai.pt.goodjob.service;

import com.wacai.pt.goodjob.common.utils.StringUtils;
import com.wacai.pt.goodjob.interior.remote.bean.TaskConfigBean;
import com.wacai.pt.goodjob.interior.remote.bean.TaskHandleBean;
import com.wacai.pt.goodjob.interior.remote.service.ScheduleService;
import com.wacai.pt.goodjob.mapper.TaskConfigMapper;
import com.wacai.pt.goodjob.vo.TaskConfigDropVo;
import com.wacai.pt.goodjob.vo.TaskConfigVo;
import com.wacai.pt.goodjob.vo.args.TaskConfigSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuanwu on 16/3/25.
 */
@Component("taskConfigService")
public class TaskConfigService {
    @Autowired
    private TaskConfigMapper taskConfigMapper;

    @Autowired
    private ScheduleService scheduleService;

    public List<TaskConfigVo> findTaskConfigs(TaskConfigSearchVo taskConfigSearchVo){
        if(!StringUtils.isNullOrBlank(taskConfigSearchVo.getTask_name())){
            taskConfigSearchVo.setTask_name("%" + taskConfigSearchVo.getTask_name() + "%");
        }
        if(!StringUtils.isNullOrBlank(taskConfigSearchVo.getTask_group())){
            taskConfigSearchVo.setTask_group("%" + taskConfigSearchVo.getTask_group() + "%");
        }
        return taskConfigMapper.findTaskConfigs(taskConfigSearchVo);
    }

    /**
     * 添加任务
     * @param taskConfigBean
     */
    public void addTask(TaskConfigBean taskConfigBean) throws Exception{
        scheduleService.addTask(taskConfigBean);
    }

    /**
     * 修改任务
     * @param taskConfigBean
     */
    public void modifyTask(TaskConfigBean taskConfigBean) throws Exception{
        scheduleService.modifyTask(taskConfigBean);
    }

    /**
     * 更新cron表达式
     * @param taskId
     * @param cron_exp
     * @throws Exception
     */
    public void updateCron(Integer taskId, String cron_exp) throws Exception{
        TaskHandleBean taskHandleBean = new TaskHandleBean();
        taskHandleBean.setId(taskId);
        taskHandleBean.setCron_exp(cron_exp);

        scheduleService.updateCron(taskHandleBean);
    }

    /**
     * 删除任务
     * @param taskId
     */
    public void deleteTask(Integer taskId) throws Exception{
        TaskHandleBean taskHandleBean = new TaskHandleBean();
        taskHandleBean.setId(taskId);

        scheduleService.deleteTask(taskHandleBean);
    }

    /**
     * 停用任务
     * @param taskId
     */
    public void stopTask(Integer taskId) throws Exception{
        TaskHandleBean taskHandleBean = new TaskHandleBean();
        taskHandleBean.setId(taskId);

        scheduleService.stopTask(taskHandleBean);
    }

    /**
     * 启用任务
     * @param taskId
     */
    public void startTask(Integer taskId) throws Exception{
        TaskHandleBean taskHandleBean = new TaskHandleBean();
        taskHandleBean.setId(taskId);

        scheduleService.startTask(taskHandleBean);
    }

    /**
     * 手动执行
     * @param taskId
     */
    public void triggerTask(Integer taskId) throws Exception{
        TaskHandleBean taskHandleBean = new TaskHandleBean();
        taskHandleBean.setId(taskId);

        scheduleService.triggerTask(taskHandleBean);
    }

    /**
     * 通过项目ID查询任务
     * @param projectId
     * @return
     */
    public List<TaskConfigDropVo> findTaskConfigByProjectId(Integer projectId){
        return taskConfigMapper.findTaskConfigByProjectId(projectId);
    }

    /**
     * 通过ID查询任务
     * @param id
     * @return
     */
    public TaskConfigVo findTaskConfigById(Integer id){
        return taskConfigMapper.findTaskConfigById(id);
    }
}
