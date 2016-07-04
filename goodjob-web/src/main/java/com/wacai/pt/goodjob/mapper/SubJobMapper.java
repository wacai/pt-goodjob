package com.wacai.pt.goodjob.mapper;

import com.wacai.pt.goodjob.vo.SubJobVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuanwu on 16/3/29.
 */
@Repository
public interface SubJobMapper {
    List<SubJobVo> findSubJobs(@Param("task_exec_id") Long task_exec_id);
}
