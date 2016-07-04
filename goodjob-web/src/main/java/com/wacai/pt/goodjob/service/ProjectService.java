package com.wacai.pt.goodjob.service;

import com.wacai.pt.goodjob.mapper.ProjectMapper;
import com.wacai.pt.goodjob.vo.ProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuanwu on 16/4/1.
 */
@Component("projectService")
public class ProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    public List<ProjectVo> findAllProject(){
        return projectMapper.findAllProject();
    }

    public void insertProject(ProjectVo projectVo){
        projectMapper.insertProject(projectVo);
    }
}
