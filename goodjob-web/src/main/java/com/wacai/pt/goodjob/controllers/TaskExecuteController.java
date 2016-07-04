package com.wacai.pt.goodjob.controllers;

import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.service.TaskExecuteService;
import com.wacai.pt.goodjob.vo.TaskExecuteVo;
import com.wacai.pt.goodjob.vo.args.TaskExecuteSearchVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xuanwu on 16/4/1.
 */
@RestController
@RequestMapping(value = "/taskExecute")
public class TaskExecuteController {
    private static final Logger logger     = LoggerFactory.getLogger(TaskExecuteController.class);

    private JsonMapper          jsonMapper = JsonMapper.getNonNullBinder();

    @Autowired
    private TaskExecuteService  taskExecuteService;


    /**
     * 查询任务执行
     * @param taskExecuteSearchVo
     * @return
     */
    @RequestMapping(value = "/find_task_execute.html", method = RequestMethod.POST)
    public List<TaskExecuteVo> findTaskConfig(@RequestBody TaskExecuteSearchVo taskExecuteSearchVo) {
        return taskExecuteService.findTaskExecutes(taskExecuteSearchVo);
    }

}
