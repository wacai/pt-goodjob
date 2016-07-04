package com.wacai.pt.goodjob.service;

import com.wacai.pt.goodjob.interior.remote.bean.HostHandleBean;
import com.wacai.pt.goodjob.interior.remote.bean.TaskHandleBean;
import com.wacai.pt.goodjob.interior.remote.service.ScheduleService;
import com.wacai.pt.goodjob.mapper.TaskHostMapper;
import com.wacai.pt.goodjob.model.HostModel;
import com.wacai.pt.goodjob.vo.TaskHostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuanwu on 16/3/31.
 */
@Component("taskHostService")
public class TaskHostService {
    @Autowired
    private TaskHostMapper taskHostMapper;

    @Autowired
    private ScheduleService scheduleService;

    public List<TaskHostVo> findTaskHostById(Integer task_config_id) {
        return taskHostMapper.findTaskHostById(task_config_id);
    }

    public void addHost(HostModel model){
        taskHostMapper.insertHostModel(model);
    }

    public void startHost(HostHandleBean hostHandleBean) throws Exception{
        scheduleService.startHost(hostHandleBean);
    }

    public void stopHost(HostHandleBean hostHandleBean) throws Exception{
        scheduleService.stopHost(hostHandleBean);
    }

    public void assignHostOwner(HostHandleBean hostHandleBean) throws Exception{
        scheduleService.assignHostOwner(hostHandleBean);
    }

    public void cancelHostOwner(HostHandleBean hostHandleBean) throws Exception{
        scheduleService.cancelHostOwner(hostHandleBean);
    }

    public void triggerDetect(Integer task_config_id) throws Exception{
        TaskHandleBean taskHandleBean = new TaskHandleBean();
        taskHandleBean.setId(task_config_id);

        scheduleService.triggerDetect(taskHandleBean);
    }
}
