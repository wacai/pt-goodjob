package com.wacai.pt.goodjob.mapper;

import com.wacai.pt.goodjob.vo.ProjectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xuanwu on 16/4/1.
 */
@Repository
public interface ProjectMapper {
    List<ProjectVo> findAllProject();

    void insertProject(ProjectVo projectVo);
}
