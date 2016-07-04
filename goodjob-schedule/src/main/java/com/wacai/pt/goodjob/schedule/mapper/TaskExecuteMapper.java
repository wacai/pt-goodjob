package com.wacai.pt.goodjob.schedule.mapper;

import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.schedule.model.TaskExecuteModel;
import com.wacai.pt.goodjob.schedule.vo.LastTaskExecuteVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component("taskExecuteMapper")
public interface TaskExecuteMapper {

    void insertTaskExecute(TaskExecuteModel taskExecuteModel);

    LastTaskExecuteVo findLastTaskExecuteByTaskId(@Param("taskId")Integer taskId);

    void updateExecuteRespById(ExecuteResponse executeResponse);
}
