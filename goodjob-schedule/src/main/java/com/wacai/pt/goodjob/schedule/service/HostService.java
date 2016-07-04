package com.wacai.pt.goodjob.schedule.service;

import com.wacai.pt.goodjob.interior.remote.bean.HostHandleBean;
import com.wacai.pt.goodjob.schedule.model.HostModel;

import java.util.List;

/**
 * Created by xuanwu on 16/5/11.
 */
public interface HostService {
    void stopHostById(HostHandleBean hostHandleBean);

    void startHostById(HostHandleBean hostHandleBean);

    Integer insertIp(String ip, Integer taskConfigId);

    void insertHost(HostModel hostModel);

    void updateTimeByUnique(String ip, Integer taskConfigId);

    void updateTimeById(Integer id);

    Integer findIdByUnique(String ip, Integer taskConfigId);

    void batchUpdateByIds(List<Integer> ids);
}
