package com.wacai.pt.goodjob.schedule.service.impl;

import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.schedule.mapper.SubJobMapper;
import com.wacai.pt.goodjob.schedule.model.SubJobModel;
import com.wacai.pt.goodjob.schedule.service.SubJobService;
import com.wacai.pt.goodjob.schedule.util.ThreadUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("subJobService")
public class SubJobServiceImpl implements SubJobService {

    @Resource
    private SubJobMapper jobMapper;

    public void insertSubJob(SubJobModel subJobModel) throws Exception{
        jobMapper.insertSubJob(subJobModel);
    }

    public void updateSubJobRespById(ExecuteResponse executeResponse){
        jobMapper.updateSubJobRespById(executeResponse);
    }

    public List<Integer> findStateByTaskExecuteId(Long taskExecuteId){
        return jobMapper.findStateByTaskExecuteId(taskExecuteId);
    }

    public void updateHostIpById(final String hostIp, final Long jobId){
        try {
            ThreadUtil.ignoreThreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    ExecuteResponse executeResponse = new ExecuteResponse();
                    executeResponse.setJobId(jobId);
                    executeResponse.setHostIp(hostIp);

                    jobMapper.updateHostIpById(executeResponse);
                }
            });
        }catch (Throwable e){
            //忽略
        }
    }
}
