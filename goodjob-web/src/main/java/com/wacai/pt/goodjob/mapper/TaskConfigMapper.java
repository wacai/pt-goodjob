package com.wacai.pt.goodjob.mapper;

import com.wacai.pt.goodjob.interior.remote.bean.TaskConfigBean;
import com.wacai.pt.goodjob.vo.TaskConfigDropVo;
import com.wacai.pt.goodjob.vo.TaskConfigVo;
import com.wacai.pt.goodjob.vo.args.TaskConfigSearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuanwu on 16/3/25.
 */
@Repository
public interface TaskConfigMapper {
    List<TaskConfigVo> findTaskConfigs(TaskConfigSearchVo taskConfigSearchVo);

    List<TaskConfigDropVo> findTaskConfigByProjectId(@Param("projectId") Integer projectId);

    TaskConfigVo findTaskConfigById(@Param("id") Integer id);
}
