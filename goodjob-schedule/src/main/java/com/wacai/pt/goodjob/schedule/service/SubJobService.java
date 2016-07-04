package com.wacai.pt.goodjob.schedule.service;

import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.schedule.model.SubJobModel;
import com.wacai.pt.goodjob.schedule.vo.SubJobVo;

import java.util.List;
import java.util.Map;

/**
 * Created by xuanwu on 16/4/10.
 */
public interface SubJobService {

    void insertSubJob(SubJobModel subJobModel) throws Exception;

    void updateSubJobRespById(ExecuteResponse executeResponse);

    List<Integer> findStateByTaskExecuteId(Long taskExecuteId);

    void updateHostIpById(final String hostIp, final Long jobId);
}
