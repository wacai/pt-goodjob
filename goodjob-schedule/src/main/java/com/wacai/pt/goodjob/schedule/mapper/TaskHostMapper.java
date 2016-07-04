package com.wacai.pt.goodjob.schedule.mapper;

import com.wacai.pt.goodjob.interior.remote.bean.HostHandleBean;
import com.wacai.pt.goodjob.schedule.model.HostModel;
import com.wacai.pt.goodjob.schedule.vo.HostVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by xuanwu on 16/4/5.
 */
@Component("taskHostMapper")
public interface TaskHostMapper {
    void stopHostById(HostHandleBean hostHandleBean);

    void startHostById(HostHandleBean hostHandleBean);

    void cancelHostOwnerByTaskId(HostHandleBean hostHandleBean);

    void cancelHostOwnerById(HostHandleBean hostHandleBean);

    void assignHostOwnerById(HostHandleBean hostHandleBean);

    void insertIp(HostModel hostModel);

    void updateTimeByUnique(Map<String, Object> parameters);

    void updateTimeById(Integer id);

    Integer findIdByUnique(Map<String, Object> parameters);

    void batchUpdateByIds(List<Integer> ids);

    List<HostVo> loadHosts();

    List<HostVo> findHostsByTaskId(Integer taskId);

    HostVo findHostsById(Integer Id);

}
