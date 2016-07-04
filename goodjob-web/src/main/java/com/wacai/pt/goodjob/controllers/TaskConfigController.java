package com.wacai.pt.goodjob.controllers;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.common.utils.StringUtils;
import com.wacai.pt.goodjob.interior.remote.bean.TaskConfigBean;
import com.wacai.pt.goodjob.service.TaskConfigService;
import com.wacai.pt.goodjob.vo.TaskConfigDropVo;
import com.wacai.pt.goodjob.vo.TaskConfigVo;
import com.wacai.pt.goodjob.vo.args.TaskConfigSearchVo;
import com.wacai.pt.goodjob.vo.resp.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanwu on 16/3/25.
 */
@RestController
@RequestMapping(value = "/taskConfig")
public class TaskConfigController {

    private static final Logger logger     = LoggerFactory.getLogger(TaskConfigController.class);

    private JsonMapper          jsonMapper = JsonMapper.getNonNullBinder();

    @Autowired
    private TaskConfigService   taskConfigService;

    /**
     * 添加任务
     *
     * @param taskConfigBean
     * @return
     */
    @RequestMapping(value = "/add_task.html", method = RequestMethod.POST)
    public ResponseVo addTask(@RequestBody TaskConfigBean taskConfigBean) {
        logger.info("============= addTask TaskConfigBean:{}", jsonMapper.toJson(taskConfigBean));
        try {
            verify(taskConfigBean, false);
            taskConfigService.addTask(taskConfigBean);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    private void verify(TaskConfigBean taskConfigBean, boolean isModify) throws Exception {
        do {

            if (!isModify && (taskConfigBean.getProject_id() == null || taskConfigBean.getProject_id().intValue() == -1)) {
                throw new Exception("请选择项目.");
            }

            if (StringUtils.isNullOrBlank(taskConfigBean.getTask_name())) {
                throw new Exception("【任务名称】不能为空.");
            }

            if (StringUtils.isNullOrBlank(taskConfigBean.getCron_exp())) {
                throw new Exception("【cron表达式】不能为空.");
            }

            if (StringUtils.isNullOrBlank(taskConfigBean.getTask_group())) {
                throw new Exception("【dubbo group】不能为空.");
            }

            if (taskConfigBean.getTask_group().endsWith("_")) {
                throw new Exception("【dubbo group】命名规范有问题.");
            }

            if (Constants.DELAY_SKIP_YES == taskConfigBean.getDelay_skip().intValue()
                && Constants.EXACT_ONCE_NO == taskConfigBean.getExact_once().intValue()) {
                if (taskConfigBean.getTimeout() == null) {
                    throw new Exception("【精确执行】选中【否】, 【超时】为必填项.");
                }
            }

            if (Constants.TASK_CONFIG_TYPE_1 == taskConfigBean.getType().intValue()) {
                if (taskConfigBean.getParamList() == null || taskConfigBean.getParamList().size() == 0){
                    throw new Exception("请填写分片参数.");
                }
            }

            if (Constants.TASK_CONFIG_TYPE_2 == taskConfigBean.getType().intValue()) {
                if (StringUtils.isNullOrBlank(taskConfigBean.getJob_data())){
                    throw new Exception("请填写任务依赖关系.");
                }
            }
        } while (false);
    }

    /**
     * 修改任务
     *
     * @param taskConfigBean
     * @return
     */
    @RequestMapping(value = "/modify_task.html", method = RequestMethod.POST)
    public ResponseVo modifyTask(@RequestBody TaskConfigBean taskConfigBean) {
        logger
            .info("============= modifyTask TaskConfigBean:{}", jsonMapper.toJson(taskConfigBean));
        try {
            verify(taskConfigBean, true);
            taskConfigBean.setIsEffect(false);
            taskConfigService.modifyTask(taskConfigBean);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 修改任务并生效
     *
     * @param taskConfigBean
     * @return
     */
    @RequestMapping(value = "/modify_and_effect_task.html", method = RequestMethod.POST)
    public ResponseVo modifyAndEffectTask(@RequestBody TaskConfigBean taskConfigBean) {
        logger.info("============= modifyAndEffectTask TaskConfigBean:{}",
                jsonMapper.toJson(taskConfigBean));
        try {
            verify(taskConfigBean, true);
            taskConfigBean.setIsEffect(true);
            taskConfigService.modifyTask(taskConfigBean);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 更新cron表达式
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/update_cron.html", method = RequestMethod.POST)
    public ResponseVo updateCron(@RequestParam Integer taskId, String cron_exp) {
        logger.info("============= deleteTask taskId:{}", taskId);
        try {
            taskConfigService.updateCron(taskId, cron_exp);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 删除任务
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/delete_task.html", method = RequestMethod.POST)
    public ResponseVo deleteTask(@RequestParam Integer taskId) {
        logger.info("============= deleteTask taskId:{}", taskId);
        try {
            taskConfigService.deleteTask(taskId);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 重启任务
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/restart_task.html", method = RequestMethod.POST)
    public ResponseVo restartTask(@RequestParam Integer taskId) {
        logger.info("============= restart_task taskId:{}", taskId);
        try {
            taskConfigService.stopTask(taskId);
            taskConfigService.startTask(taskId);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 停用任务
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/stop_task.html", method = RequestMethod.POST)
    public ResponseVo stopTask(@RequestParam Integer taskId) {
        logger.info("============= stopTask taskId:{}", taskId);
        try {
            taskConfigService.stopTask(taskId);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 启用任务
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/start_Task.html", method = RequestMethod.POST)
    public ResponseVo startTask(@RequestParam Integer taskId) {
        logger.info("============= startTask taskId:{}", taskId);
        try {
            taskConfigService.startTask(taskId);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 手动执行
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/trigger_task.html", method = RequestMethod.POST)
    public ResponseVo triggerTask(@RequestParam Integer taskId) {
        logger.info("============= triggerTask taskId:{}", taskId);
        try {
            taskConfigService.triggerTask(taskId);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     * 通过ID查询任务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/find_task_config_by_id.html", method = RequestMethod.POST)
    public TaskConfigVo findTaskConfig(@RequestParam Integer id) {
        return taskConfigService.findTaskConfigById(id);
    }

    /**
     * 查询配置
     *
     * @param taskConfigSearchVo
     * @return
     */
    @RequestMapping(value = "/find_task_config.html", method = RequestMethod.POST)
    public List<TaskConfigVo> findTaskConfig(@RequestBody TaskConfigSearchVo taskConfigSearchVo) {
        if (StringUtils.isNullOrBlank(taskConfigSearchVo.getTask_name())
            && taskConfigSearchVo.getProject_id() == null
            && StringUtils.isNullOrBlank(taskConfigSearchVo.getTask_group())) {
            return new ArrayList<TaskConfigVo>();
        }
        List<TaskConfigVo> taskConfigVos = taskConfigService.findTaskConfigs(taskConfigSearchVo);
        return taskConfigVos;
    }

    /**
     * 通过项目ID查询任务配置（下拉）
     *
     * @return
     */
    @RequestMapping(value = "/find_task_config_drop.html", method = RequestMethod.POST)
    public List<TaskConfigDropVo> findTaskConfigDrop(@RequestParam Integer projectId) {
        return taskConfigService.findTaskConfigByProjectId(projectId);
    }
}
