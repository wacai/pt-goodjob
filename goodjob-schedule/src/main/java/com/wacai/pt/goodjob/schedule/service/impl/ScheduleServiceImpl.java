package com.wacai.pt.goodjob.schedule.service.impl;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.common.utils.StringUtils;
import com.wacai.pt.goodjob.interior.remote.bean.*;
import com.wacai.pt.goodjob.interior.remote.service.ScheduleService;
import com.wacai.pt.goodjob.schedule.detect.DetectBean;
import com.wacai.pt.goodjob.schedule.detect.DetectHost;
import com.wacai.pt.goodjob.schedule.intern.SchedualManager;
import com.wacai.pt.goodjob.schedule.intern.ScheduleExecute;
import com.wacai.pt.goodjob.schedule.intern.WacQuartzJob;
import com.wacai.pt.goodjob.schedule.mapper.TaskConfigMapper;
import com.wacai.pt.goodjob.schedule.mapper.TaskHostMapper;
import com.wacai.pt.goodjob.schedule.mapper.TaskParamMapper;
import com.wacai.pt.goodjob.schedule.model.TaskConfigModel;
import com.wacai.pt.goodjob.schedule.model.TaskParamModel;
import com.wacai.pt.goodjob.schedule.util.HostsUtil;
import com.wacai.pt.goodjob.schedule.vo.HostVo;
import com.wacai.pt.goodjob.schedule.vo.TaskConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xuanwu on 16/4/1.
 */
@Component("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
    private static final Logger logger     = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    private static JsonMapper   jsonMapper = JsonMapper.getNonNullBinder();

    @Autowired
    private SchedualManager     schedualManager;

    @Autowired
    private TaskParamMapper     taskParamMapper;

    @Autowired
    private TaskConfigMapper    taskConfigMapper;

    @Autowired
    private TaskHostMapper      taskHostMapper;

    /**
     * 添加任务
     *
     * @param taskConfigBean
     */
    @Override
    public void addTask(TaskConfigBean taskConfigBean) throws Exception {
        logger.info("---------添加任务----------taskConfigBean:{}", jsonMapper.toJson(taskConfigBean));
        TaskConfigModel taskConfigModel = new TaskConfigModel(taskConfigBean);
        taskConfigMapper.insertTaskConfig(taskConfigModel);
        if (taskConfigBean.getParamList() != null) {
            for (String param : taskConfigBean.getParamList()) {
                TaskParamModel taskParamModel = new TaskParamModel();
                taskParamModel.setParam(param);
                taskParamModel.setTaskConfigId(taskConfigModel.getId());
                taskParamModel.setLastModifyBy(taskConfigBean.getLastModifyBy());
                taskParamMapper.insertTaskParam(taskParamModel);
            }
        }

        DetectHost.getDetectHost().getTaskMap().put(String.valueOf(taskConfigModel.getId()), new DetectBean(taskConfigModel.getTask_group(), 0));
        DetectHost.getDetectHost().detect(taskConfigModel.getId(), taskConfigModel.getTask_group());
    }

    /**
     * 修改任务
     *
     * @param taskConfigBean
     */
    @Override
    public void modifyTask(TaskConfigBean taskConfigBean) throws Exception {
        logger.info("---------修改任务----------taskConfigBean:{}", jsonMapper.toJson(taskConfigBean));
        taskConfigMapper.updateTaskConfigById(taskConfigBean);
        if (Constants.TASK_CONFIG_TYPE_1 == taskConfigBean.getType().intValue()) {
            taskParamMapper.deleteTaskParamByTaskId(taskConfigBean.getId());
            for (String param : taskConfigBean.getParamList()) {
                TaskParamModel taskParamModel = new TaskParamModel();
                taskParamModel.setParam(param);
                taskParamModel.setTaskConfigId(taskConfigBean.getId());
                taskParamModel.setLastModifyBy(taskConfigBean.getLastModifyBy());
                taskParamMapper.insertTaskParam(taskParamModel);
            }
        }
        if (taskConfigBean.isEffect()) {
            logger.info("------<<<<<<<---修改并生效----->>>>>>>>-----");
            TaskHandleBean taskHandleBean = new TaskHandleBean();
            taskHandleBean.setId(taskConfigBean.getId());
            taskHandleBean.setLastModifyBy(taskConfigBean.getLastModifyBy());

            stopTask(taskHandleBean);
            TaskConfigVo taskConfigVo = taskConfigMapper
                .findSchedConfigWithoutStateById(taskConfigBean.getId());
            ScheduleExecute.taskConfigMap.put(String.valueOf(taskConfigBean.getId()), taskConfigVo);
            startTask(taskHandleBean);

            DetectHost.getDetectHost().getTaskMap().put(String.valueOf(taskConfigVo.getId()), new DetectBean(taskConfigVo.getTask_group(), 1));
            DetectHost.getDetectHost().detect(taskConfigVo.getId(), taskConfigVo.getTask_group());
        }
    }

    /**
     * 删除任务
     *
     * @param taskHandleBean
     */
    @Override
    public void deleteTask(TaskHandleBean taskHandleBean) throws Exception {
        logger.info("---------删除任务----------taskHandleBean:{}", jsonMapper.toJson(taskHandleBean));
        taskConfigMapper.deleteTaskConfigById(taskHandleBean);
        schedualManager.removeTask(taskHandleBean.getId());

        DetectHost.getDetectHost().getTaskMap().remove(String.valueOf(taskHandleBean.getId()));
    }

    /**
     * 停用任务
     *
     * @param taskHandleBean
     */
    @Override
    public void stopTask(TaskHandleBean taskHandleBean) throws Exception {
        logger.info("---------停用任务----------taskHandleBean:{}", jsonMapper.toJson(taskHandleBean));
        taskConfigMapper.stopTaskConfigById(taskHandleBean);
        schedualManager.removeTask(taskHandleBean.getId());

        DetectBean detectBean = DetectHost.getDetectHost().getTaskMap().get(String.valueOf(taskHandleBean.getId()));
        if (detectBean != null){
            detectBean.setTask_state(0);
            detectBean.setCreateTime(System.currentTimeMillis());
        }
    }

    /**
     * 启用任务
     *
     * @param taskHandleBean
     */
    @Override
    public void startTask(TaskHandleBean taskHandleBean) throws Exception {
        logger.info("---------启用任务----------taskHandleBean:{}", jsonMapper.toJson(taskHandleBean));
        taskConfigMapper.startTaskConfigById(taskHandleBean);
        TaskConfigVo taskConfigVo = taskConfigMapper.findSchedConfigWithoutStateById(taskHandleBean
            .getId());
        schedualManager.addTask(taskConfigVo);

        DetectHost.getDetectHost().getTaskMap().put(String.valueOf(taskConfigVo.getId()), new DetectBean(taskConfigVo.getTask_group(), 1));
        DetectHost.getDetectHost().detect(taskConfigVo.getId(), taskConfigVo.getTask_group());
    }

    /**
     * 更新cron表达式
     *
     * @param taskHandleBean
     */
    @Override
    public void updateCron(TaskHandleBean taskHandleBean) throws Exception {
        if (!StringUtils.isNullOrBlank(taskHandleBean.getCron_exp())) {
            taskConfigMapper.updateCron_expById(taskHandleBean);
            schedualManager.reScheduleJob(taskHandleBean.getId(), taskHandleBean.getCron_exp());
        } else {
            throw new Exception("Invalid cron_exp.");
        }
    }

    /**
     * 手动执行
     *
     * @param taskHandleBean
     */
    @Override
    public void triggerTask(TaskHandleBean taskHandleBean) throws Exception {
        WacQuartzJob wacQuartzJob = new WacQuartzJob();
        Map<String, Object> map = new HashMap<>();
        map.put("taskId", taskHandleBean.getId());
        map.put("taskName", "手动执行.");
        map.put("schedTime", System.currentTimeMillis());
        map.put("execType", Constants.EXEC_TYPE_MANU);

        logger.info("---------手动执行----------map:{}", jsonMapper.toJson(map));
        wacQuartzJob.jobExecute(map);
    }


    /**
     * 手动探测
     *
     * @param taskHandleBean
     */
    @Override
    public void triggerDetect(TaskHandleBean taskHandleBean) throws Exception {
        TaskConfigVo taskConfigVo = taskConfigMapper
                .findSchedConfigWithoutStateById(taskHandleBean.getId());
        if (taskConfigVo != null){
            DetectHost.getDetectHost().detect(taskConfigVo.getId(), taskConfigVo.getTask_group(), true);
        }
    }

    /**
     * 禁用执行机
     *
     * @param hostHandleBean
     */
    @Override
    public void stopHost(HostHandleBean hostHandleBean) throws Exception {
        logger.info("---------停用主机----------hostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        taskHostMapper.stopHostById(hostHandleBean);

        //添加禁用
        HostVo hostVo = taskHostMapper.findHostsById(hostHandleBean.getId());
        List<String> disableHosts = HostsUtil.getHostsUtil().getDisableHosts()
            .get(String.valueOf(hostVo.getTask_config_id()));

        List<String> ownerHosts = HostsUtil.getHostsUtil().getOwnerHosts()
                .get(String.valueOf(hostVo.getTask_config_id()));
        if (ownerHosts != null) {
            for (int i = 0; i < ownerHosts.size(); i++) {
                if (hostVo.getIp().equals(ownerHosts.get(i))) {
                    ownerHosts.remove(i);
                    i--;
                }
            }

            if (ownerHosts.size() == 0){
                HostsUtil.getHostsUtil().getOwnerHosts().remove(String.valueOf(hostVo.getTask_config_id()));
            }
        }

        if (disableHosts != null) {
            for (int i = 0; i < disableHosts.size(); i++) {
                if (hostVo.getIp().equals(disableHosts.get(i))) {
                    disableHosts.remove(i);
                    i--;
                }
            }
        } else {
            disableHosts = new CopyOnWriteArrayList<>();
            HostsUtil.getHostsUtil().getDisableHosts()
                .put(String.valueOf(hostVo.getTask_config_id()), disableHosts);
        }

        disableHosts.add(hostVo.getIp());
    }

    /**
     * 启用执行机
     *
     * @param hostHandleBean
     */
    @Override
    public void startHost(HostHandleBean hostHandleBean) throws Exception {
        logger.info("---------启用主机----------hostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        taskHostMapper.startHostById(hostHandleBean);

        //排除禁用
        HostVo hostVo = taskHostMapper.findHostsById(hostHandleBean.getId());
        List<String> disableHosts = HostsUtil.getHostsUtil().getDisableHosts()
            .get(String.valueOf(hostVo.getTask_config_id()));
        if (disableHosts != null) {
            for (int i = 0; i < disableHosts.size(); i++) {
                if (hostVo.getIp().equals(disableHosts.get(i))) {
                    disableHosts.remove(i);
                    i--;
                }
            }

            if (disableHosts.size() == 0){
                HostsUtil.getHostsUtil().getDisableHosts().remove(String.valueOf(hostVo.getTask_config_id()));
            }
        }
    }

    /**
     * 指定执行机
     * @param hostHandleBean
     * @throws Exception
     */
    public void assignHostOwner(HostHandleBean hostHandleBean) throws Exception {
        logger.info("---------指定执行机----------hostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        taskHostMapper.assignHostOwnerById(hostHandleBean);

        HostVo hostVo = taskHostMapper.findHostsById(hostHandleBean.getId());
        List<String> ownerHosts = HostsUtil.getHostsUtil().getOwnerHosts()
            .get(String.valueOf(hostVo.getTask_config_id()));

        List<String> disableHosts = HostsUtil.getHostsUtil().getDisableHosts()
                .get(String.valueOf(hostVo.getTask_config_id()));
        if(disableHosts != null){
            for (int i = 0; i < disableHosts.size(); i++) {
                if (hostVo.getIp().equals(disableHosts.get(i))) {
                    disableHosts.remove(i);
                    i--;
                }
            }

            if (disableHosts.size() == 0){
                HostsUtil.getHostsUtil().getDisableHosts().remove(String.valueOf(hostVo.getTask_config_id()));
            }
        }

        if (ownerHosts != null) {
            for (int i = 0; i < ownerHosts.size(); i++) {
                if (hostVo.getIp().equals(ownerHosts.get(i))) {
                    ownerHosts.remove(i);
                    i--;
                }
            }
        } else {
            ownerHosts = new CopyOnWriteArrayList<>();
            HostsUtil.getHostsUtil().getOwnerHosts()
                    .put(String.valueOf(hostVo.getTask_config_id()), ownerHosts);
        }

        ownerHosts.add(hostVo.getIp());
    }

    /**
     * 取消指定执行机
     * @param hostHandleBean
     * @throws Exception
     */
    public void cancelHostOwner(HostHandleBean hostHandleBean) throws Exception {
        logger.info("---------取消指定执行机----------hostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        taskHostMapper.cancelHostOwnerById(hostHandleBean);

        HostVo hostVo = taskHostMapper.findHostsById(hostHandleBean.getId());
        List<String> ownerHosts = HostsUtil.getHostsUtil().getOwnerHosts()
                .get(String.valueOf(hostVo.getTask_config_id()));
        if (ownerHosts != null) {
            for (int i = 0; i < ownerHosts.size(); i++) {
                if (hostVo.getIp().equals(ownerHosts.get(i))) {
                    ownerHosts.remove(i);
                    i--;
                }
            }

            if (ownerHosts.size() == 0){
                HostsUtil.getHostsUtil().getOwnerHosts().remove(String.valueOf(hostVo.getTask_config_id()));
            }
        }
    }

//    private void reLoadDisableHosts(Integer taskId) {
//        HostsUtil.getHostsUtil().getDisableHosts().remove(String.valueOf(taskId));
//        List<HostVo> hostVos = taskHostMapper.findHostsByTaskId(taskId);
//        List<String> disableHosts = new CopyOnWriteArrayList<>();
//        for (HostVo hostVo : hostVos) {
//            if (Constants.COMMON_YES == hostVo.getDisabled().intValue()) {
//                disableHosts.add(hostVo.getIp());
//            }
//        }
//        if (disableHosts.size() > 0) {
//            HostsUtil.getHostsUtil().getDisableHosts().put(String.valueOf(taskId), disableHosts);
//        }
//    }

    /**
     * 添加参数
     *
     * @param taskParamBean
     */
    @Override
    public void addParam(TaskParamBean taskParamBean) throws Exception {
        logger.info("---------添加参数----------taskParamBean:{}", jsonMapper.toJson(taskParamBean));
        taskParamMapper.insertTaskParam(new TaskParamModel(taskParamBean));
    }

    /**
     * 修改参数
     *
     * @param taskParamBean
     */
    @Override
    public void modifyParam(TaskParamBean taskParamBean) throws Exception {
        logger.info("---------修改参数----------taskParamBean:{}", jsonMapper.toJson(taskParamBean));
        taskParamMapper.updateTaskParamById(taskParamBean);
    }

    /**
     * 删除参数
     *
     * @param paramHandleBean
     */
    @Override
    public void deleteParam(ParamHandleBean paramHandleBean) throws Exception {
        logger
            .info("---------删除参数----------paramHandleBean:{}", jsonMapper.toJson(paramHandleBean));
        taskParamMapper.deleteTaskParamById(paramHandleBean.getId());
    }
}
