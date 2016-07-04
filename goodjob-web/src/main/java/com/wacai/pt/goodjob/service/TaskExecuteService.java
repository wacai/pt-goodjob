package com.wacai.pt.goodjob.service;

import com.wacai.pt.goodjob.mapper.TaskExecuteMapper;
import com.wacai.pt.goodjob.vo.TaskExecuteVo;
import com.wacai.pt.goodjob.vo.args.TaskExecuteSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuanwu on 16/3/31.
 */
@Component("taskExecuteService")
public class TaskExecuteService {
    @Autowired
    private TaskExecuteMapper taskExecuteMapper;

    public List<TaskExecuteVo> findTaskExecutes(TaskExecuteSearchVo taskExecuteSearchVo){
        return taskExecuteMapper.findTaskExecutes(taskExecuteSearchVo);
    }
}
