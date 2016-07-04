package com.wacai.pt.goodjob.controllers;

import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.interior.remote.bean.HostHandleBean;
import com.wacai.pt.goodjob.model.HostModel;
import com.wacai.pt.goodjob.service.TaskHostService;
import com.wacai.pt.goodjob.vo.TaskHostVo;
import com.wacai.pt.goodjob.vo.resp.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by xuanwu on 16/4/5.
 */
@RestController
@RequestMapping(value = "/taskHost")
public class TaskHostController {

    private static final Logger logger     = LoggerFactory.getLogger(TaskHostController.class);

    private final static String ipRegEx  = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";

    private JsonMapper jsonMapper = JsonMapper.getNonNullBinder();

    @Autowired
    private TaskHostService taskHostService;


    /**
     * 查询任务主机
     * @param taskConfigId
     * @return
     */
    @RequestMapping(value = "/find_task_host.html", method = RequestMethod.POST)
    public List<TaskHostVo> findTaskHost(@RequestParam Integer taskConfigId) {
        return taskHostService.findTaskHostById(taskConfigId);
    }

    /**
     * 手动探测
     * @param taskConfigId
     * @return
     */
    @RequestMapping(value = "/detect_hosts.html", method = RequestMethod.POST)
    public ResponseVo detectHosts(@RequestParam Integer taskConfigId) {
        try {
            taskHostService.triggerDetect(taskConfigId);
        } catch (Exception e) {
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     *  启用主机
     * @param hostHandleBean
     * @return
     */
    @RequestMapping(value = "/start_host.html", method = RequestMethod.POST)
    public ResponseVo startHost(@RequestBody HostHandleBean hostHandleBean) {
        logger.info("============= startHost HostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        try {
            taskHostService.startHost(hostHandleBean);
        } catch (Exception e) {
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     *  禁用主机
     * @param hostHandleBean
     * @return
     */
    @RequestMapping(value = "/stop_host.html", method = RequestMethod.POST)
    public ResponseVo stopHost(@RequestBody HostHandleBean hostHandleBean) {
        logger.info("============= stopHost HostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        try {
            taskHostService.stopHost(hostHandleBean);
        } catch (Exception e) {
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     *  指定为执行机
     * @param hostHandleBean
     * @return
     */
    @RequestMapping(value = "/assign_host_owner.html", method = RequestMethod.POST)
    public ResponseVo assignHostOwner(@RequestBody HostHandleBean hostHandleBean) {
        logger.info("============= assignHostOwner HostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        try {
            taskHostService.assignHostOwner(hostHandleBean);
        } catch (Exception e) {
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     *  取消执行机
     * @param hostHandleBean
     * @return
     */
    @RequestMapping(value = "/cancel_host_owner.html", method = RequestMethod.POST)
    public ResponseVo cancelHostOwner(@RequestBody HostHandleBean hostHandleBean) {
        logger.info("============= cancelHostOwner HostHandleBean:{}", jsonMapper.toJson(hostHandleBean));
        try {
            taskHostService.cancelHostOwner(hostHandleBean);
        } catch (Exception e) {
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    /**
     *  添加调度机
     * @param hostModel
     * @return
     */
    @RequestMapping(value = "/add_host.html", method = RequestMethod.POST)
    public ResponseVo addHost(@RequestBody HostModel hostModel) {
        logger.info("============= addHost HostHandleBean:{}", jsonMapper.toJson(hostModel));
        try {
            verify(hostModel);
//            taskHostService.addHost(hostModel);
        } catch (Exception e) {
            return new ResponseVo("1", e.getMessage());
        }

        return new ResponseVo();
    }

    private void verify(HostModel hostModel) throws Exception {
        do {
            if (hostModel.getIp() == null || hostModel.getIp().matches(ipRegEx)) {
                throw new Exception("无法输入IP? 请点击右边的【手动探测执行机】.");
            }

        } while (false);
    }

}
