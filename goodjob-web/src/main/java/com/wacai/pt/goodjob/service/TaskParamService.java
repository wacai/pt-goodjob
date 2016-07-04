package com.wacai.pt.goodjob.service;

import com.wacai.pt.goodjob.interior.remote.bean.ParamHandleBean;
import com.wacai.pt.goodjob.interior.remote.bean.TaskParamBean;
import com.wacai.pt.goodjob.interior.remote.service.ScheduleService;
import com.wacai.pt.goodjob.mapper.TaskParamMapper;
import com.wacai.pt.goodjob.vo.TaskParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuanwu on 16/3/31.
 */
@Component("taskParamService")
public class TaskParamService {
    @Autowired
    private TaskParamMapper taskParamMapper;

    @Autowired
    private ScheduleService scheduleService;

    public List<TaskParamVo> findTaskParamVoById(Integer task_config_id){
        return taskParamMapper.findTaskParamVoById(task_config_id);
    }

    /**
     * 添加参数
     * @param taskParamBean
     * @throws Exception
     */
    public void addParam(TaskParamBean taskParamBean) throws Exception{
        scheduleService.addParam(taskParamBean);
    }

    /**
     * 修改参数
     * @param taskParamBean
     */
    public void modifyParam(TaskParamBean taskParamBean) throws Exception{
        scheduleService.modifyParam(taskParamBean);
    }

    /**
     * 删除参数
     * @param paramId
     */
    public void deleteParam(Integer paramId) throws Exception{
        ParamHandleBean paramHandleBean = new ParamHandleBean();
        paramHandleBean.setId(paramId);

        scheduleService.deleteParam(paramHandleBean);
    }
}
