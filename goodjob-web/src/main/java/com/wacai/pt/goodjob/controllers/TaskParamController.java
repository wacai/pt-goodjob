package com.wacai.pt.goodjob.controllers;

import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.interior.remote.bean.TaskParamBean;
import com.wacai.pt.goodjob.service.TaskParamService;
import com.wacai.pt.goodjob.vo.TaskParamVo;
import com.wacai.pt.goodjob.vo.resp.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by xuanwu on 16/4/1.
 */
@RestController
@RequestMapping(value = "/taskParam")
public class TaskParamController {

    private static final Logger logger     = LoggerFactory.getLogger(TaskParamController.class);

    private JsonMapper          jsonMapper = JsonMapper.getNonNullBinder();

    @Autowired
    private TaskParamService    taskParamService;

    /**
     * 查询任务执行
     * @param taskConfigId
     * @return
     */
    @RequestMapping(value = "/find_task_param.html", method = RequestMethod.POST)
    public List<TaskParamVo> findTaskParamVoById(@RequestParam Integer taskConfigId) {
        return taskParamService.findTaskParamVoById(taskConfigId);
    }

    /**
     * 添加参数
     * @param taskParamBean
     * @return
     */
    @RequestMapping(value = "/add_param.html", method = RequestMethod.POST)
    public ResponseVo addParam(@RequestBody TaskParamBean taskParamBean) {
        logger.info("============= addParam taskParamBean:{}", jsonMapper.toJson(taskParamBean));
        try {
            taskParamService.addParam(taskParamBean);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     *  修改参数
     * @param taskParamBean
     * @return
     */
    @RequestMapping(value = "/modify_param.html", method = RequestMethod.POST)
    public ResponseVo modifyParam(@RequestBody TaskParamBean taskParamBean) {
        logger.info("============= modifyParam taskParamBean:{}", jsonMapper.toJson(taskParamBean));
        try {
            taskParamService.modifyParam(taskParamBean);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     *  删除参数
     * @param paramId
     * @return
     */
    @RequestMapping(value = "/delete_param.html", method = RequestMethod.POST)
    public ResponseVo deleteParam(@RequestParam Integer paramId) {
        logger.info("============= deleteParam paramId:{}", paramId);
        try {
            taskParamService.deleteParam(paramId);
        } catch (Exception e) {
            logger.error("failed, cause:", e);
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

}
