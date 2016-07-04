package com.wacai.pt.goodjob.schedule.service;

import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.schedule.model.TaskExecuteModel;
import com.wacai.pt.goodjob.schedule.vo.LastTaskExecuteVo;
import com.wacai.pt.goodjob.schedule.vo.SubJobVo;
import com.wacai.pt.goodjob.schedule.vo.TaskExecute;

import java.util.List;

/**
 * Created by xuanwu on 16/4/10.
 */
public interface TaskExecuteService {
    void insertTaskExecute(TaskExecuteModel taskExecuteModel) throws Exception;

    LastTaskExecuteVo findLastTaskExecuteByTaskId(Integer taskId);

    void updateExecuteRespById(ExecuteResponse executeResponse);

}
