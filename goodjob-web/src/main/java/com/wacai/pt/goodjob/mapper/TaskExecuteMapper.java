package com.wacai.pt.goodjob.mapper;

import com.wacai.pt.goodjob.vo.TaskExecuteVo;
import com.wacai.pt.goodjob.vo.args.TaskExecuteSearchVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuanwu on 16/3/29.
 */
@Repository
public interface TaskExecuteMapper {
    List<TaskExecuteVo> findTaskExecutes(TaskExecuteSearchVo taskExecuteSearchVo);
}
