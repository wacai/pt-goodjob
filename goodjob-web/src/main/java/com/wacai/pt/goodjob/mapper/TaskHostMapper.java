package com.wacai.pt.goodjob.mapper;

import com.wacai.pt.goodjob.model.HostModel;
import com.wacai.pt.goodjob.vo.TaskHostVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by xuanwu on 16/3/29.
 */
@Repository
public interface TaskHostMapper {
    List<TaskHostVo> findTaskHostById(@Param("task_config_id") Integer task_config_id);

    void deleteExceedIps(@Param("lastUpdateTime") Date lastUpdateTime);

    void insertHostModel(HostModel hostModel);
}
