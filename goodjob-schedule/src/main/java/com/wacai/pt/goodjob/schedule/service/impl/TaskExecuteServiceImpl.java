package com.wacai.pt.goodjob.schedule.service.impl;

import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.schedule.mapper.TaskExecuteMapper;
import com.wacai.pt.goodjob.schedule.model.TaskExecuteModel;
import com.wacai.pt.goodjob.schedule.service.TaskExecuteService;
import com.wacai.pt.goodjob.schedule.vo.LastTaskExecuteVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("taskExecuteService")
public class TaskExecuteServiceImpl implements TaskExecuteService {

    @Resource
    private TaskExecuteMapper taskExecuteMapper;


    public void insertTaskExecute(TaskExecuteModel taskExecuteModel) throws Exception{
        taskExecuteMapper.insertTaskExecute(taskExecuteModel);
    }

    public LastTaskExecuteVo findLastTaskExecuteByTaskId(Integer taskId){
        return taskExecuteMapper.findLastTaskExecuteByTaskId(taskId);
    }

    public void updateExecuteRespById(ExecuteResponse executeResponse){
        taskExecuteMapper.updateExecuteRespById(executeResponse);
    }
}
