package com.wacai.pt.goodjob.controllers;

import com.wacai.pt.goodjob.service.ProjectService;
import com.wacai.pt.goodjob.vo.ProjectVo;
import com.wacai.pt.goodjob.vo.resp.ResponseVo;
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
@RequestMapping(value = "/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 查询项目（下拉）
     * @return
     */
    @RequestMapping(value = "/find_all_project.html", method = RequestMethod.POST)
    public List<ProjectVo> findAllProject() {
        return projectService.findAllProject();
    }

    /**
     * 添加项目
     *
     * @param projectVo
     * @return
     */
    @RequestMapping(value = "/add_project.html", method = RequestMethod.POST)
    public ResponseVo addProject(@RequestBody ProjectVo projectVo) {
        try {
            projectService.insertProject(projectVo);
        } catch (Exception e) {
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }
}
