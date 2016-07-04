package com.wacai.pt.goodjob.service;

import com.wacai.pt.goodjob.mapper.SubJobMapper;
import com.wacai.pt.goodjob.vo.SubJobVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuanwu on 16/3/31.
 */
@Component("subJobService")
public class SubJobService {
    @Autowired
    private SubJobMapper subJobMapper;

    public List<SubJobVo> findSubJobs(Long task_exec_id){
        return subJobMapper.findSubJobs(task_exec_id);
    }
}
