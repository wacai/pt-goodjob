package com.wacai.pt.goodjob.schedule.service.impl;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.interior.remote.bean.HostHandleBean;
import com.wacai.pt.goodjob.schedule.mapper.TaskHostMapper;
import com.wacai.pt.goodjob.schedule.model.HostModel;
import com.wacai.pt.goodjob.schedule.service.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuanwu on 16/5/11.
 */
@Component("hostService")
public class HostServiceImpl implements HostService {
    @Autowired
    private TaskHostMapper taskHostMapper;

    public void stopHostById(HostHandleBean hostHandleBean) {
        taskHostMapper.stopHostById(hostHandleBean);
    }

    public void startHostById(HostHandleBean hostHandleBean) {
        taskHostMapper.startHostById(hostHandleBean);
    }

    public Integer insertIp(String ip, Integer taskConfigId) {
        HostModel hostModel = new HostModel();
        hostModel.setIp(ip);
        hostModel.setDisabled(Constants.COMMON_NO);
        hostModel.setOwner(Constants.HOST_OWNER_NO);
        hostModel.setTask_config_id(taskConfigId);
        taskHostMapper.insertIp(hostModel);

        return hostModel.getId();
    }

    public void insertHost(HostModel hostModel) {
        taskHostMapper.insertIp(hostModel);
    }

    public void updateTimeByUnique(String ip, Integer taskConfigId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("taskConfigId", taskConfigId);
        parameters.put("ip", ip);

        taskHostMapper.updateTimeByUnique(parameters);
    }

    public void updateTimeById(Integer id) {
        taskHostMapper.updateTimeById(id);
    }

    public Integer findIdByUnique(String ip, Integer taskConfigId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("taskConfigId", taskConfigId);
        parameters.put("ip", ip);

        return taskHostMapper.findIdByUnique(parameters);
    }

    public void batchUpdateByIds(List<Integer> ids) {
        taskHostMapper.batchUpdateByIds(ids);
    }
}
