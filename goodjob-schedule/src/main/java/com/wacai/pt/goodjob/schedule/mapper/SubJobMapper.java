package com.wacai.pt.goodjob.schedule.mapper;

import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.schedule.model.SubJobModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Component("subJobMapper")
public interface SubJobMapper {

    void insertSubJob(SubJobModel subJobModel);

    void updateSubJobRespById(ExecuteResponse executeResponse);

    List<Integer> findStateByTaskExecuteId(@Param("taskExecuteId") Long taskExecuteId);

    void updateHostIpById(ExecuteResponse executeResponse);
}
