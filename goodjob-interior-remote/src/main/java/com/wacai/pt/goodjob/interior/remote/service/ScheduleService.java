package com.wacai.pt.goodjob.interior.remote.service;

import com.wacai.pt.goodjob.interior.remote.bean.*;

/**
 * Created by xuanwu on 16/3/25.
 */
public interface ScheduleService {

    /**
     * 添加任务
     * @param taskConfigBean
     */
    void addTask(TaskConfigBean taskConfigBean) throws Exception;

    /**
     * 修改任务
     * @param taskConfigBean
     */
    void modifyTask(TaskConfigBean taskConfigBean) throws Exception;

    /**
     * 删除任务
     * @param taskHandleBean
     */
    void deleteTask(TaskHandleBean taskHandleBean) throws Exception;

    /**
     * 停用任务
     * @param taskHandleBean
     */
    void stopTask(TaskHandleBean taskHandleBean) throws Exception;

    /**
     * 启用任务
     * @param taskHandleBean
     */
    void startTask(TaskHandleBean taskHandleBean) throws Exception;

    /**
     * 更新cron表达式
     * @param taskHandleBean
     */
    void updateCron(TaskHandleBean taskHandleBean) throws Exception;


    /**
     * 手动执行
     * @param taskHandleBean
     */
    void triggerTask(TaskHandleBean taskHandleBean) throws Exception;

    /**
     * 手动探测
     *
     * @param taskHandleBean
     */
    void triggerDetect(TaskHandleBean taskHandleBean) throws Exception;

    /**
     * 停用主机
     * @param hostHandleBean
     */
    void stopHost(HostHandleBean hostHandleBean) throws Exception;

    /**
     * 启用主机
     * @param hostHandleBean
     */
    void startHost(HostHandleBean hostHandleBean) throws Exception;

    /**
     * 指定调度机
     * @param hostHandleBean
     * @throws Exception
     */
    void assignHostOwner(HostHandleBean hostHandleBean) throws Exception;

    /**
     * 取消调度机
     * @param hostHandleBean
     * @throws Exception
     */
    void cancelHostOwner(HostHandleBean hostHandleBean) throws Exception;

    /**
     * 添加参数
     * @param taskParamBean
     */
    void addParam(TaskParamBean taskParamBean)  throws Exception;

    /**
     * 修改参数
     * @param taskParamBean
     */
    void modifyParam(TaskParamBean taskParamBean) throws Exception;

    /**
     * 删除参数
     * @param paramHandleBean
     */
    void deleteParam(ParamHandleBean paramHandleBean) throws Exception;
}
