package com.wacai.pt.goodjob.schedule.mapper;

import com.wacai.pt.goodjob.interior.remote.bean.TaskParamBean;
import com.wacai.pt.goodjob.schedule.model.TaskParamModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("taskParamMapper")
public interface TaskParamMapper {

    void updateTaskParamById(TaskParamBean taskParamBean);

    void insertTaskParam(TaskParamModel taskParamModel);

    void deleteTaskParamById(@Param("id")Integer id);

    int countParamByParam(TaskParamBean taskParamBean);
}
