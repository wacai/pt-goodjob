package com.wacai.pt.goodjob.controllers;

import com.wacai.pt.goodjob.service.SubJobService;
import com.wacai.pt.goodjob.vo.SubJobVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xuanwu on 16/4/1.
 */
@RestController
@RequestMapping(value = "/subJob")
public class SubJobController {
    @Autowired
    private SubJobService subJobService;

    /**
     * 查询任务执行
     * @param taskExecId
     * @return
     */
    @RequestMapping(value = "/find_sub_job.html", method = RequestMethod.POST)
    public List<SubJobVo> findSubJob(@RequestParam Long taskExecId) {
        return subJobService.findSubJobs(taskExecId);
    }
}
