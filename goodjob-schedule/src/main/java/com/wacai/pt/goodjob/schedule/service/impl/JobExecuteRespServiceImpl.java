package com.wacai.pt.goodjob.schedule.service.impl;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.common.utils.StringUtils;
import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.remote.service.JobExecuteRespService;
import com.wacai.pt.goodjob.schedule.service.SubJobService;
import com.wacai.pt.goodjob.schedule.service.TaskExecuteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by xuanwu on 16/4/10.
 */
@Component("jobExecuteRespService")
public class JobExecuteRespServiceImpl implements JobExecuteRespService {
    private static final Logger logger     = LoggerFactory
                                               .getLogger(JobExecuteRespServiceImpl.class);
    private static JsonMapper   jsonMapper = JsonMapper.getNonNullBinder();

    @Autowired
    private TaskExecuteService  taskExecuteService;
    @Autowired
    private SubJobService       subJobService;

    @Override
    public void executeResp(ExecuteResponse executeResponse) throws Throwable {
        logger.info(">>>>>>>>>>>>>>>>>>>> executeResponse:{}", jsonMapper.toJson(executeResponse));
        if (Constants.TASK_CONFIG_TYPE_0 == executeResponse.getTaskType().intValue()
            || executeResponse.getJobCount().intValue() == 1) {
            oneJob(executeResponse);
        } else {
            multiJob(executeResponse);
        }
    }

    /**
     *  单个job更新
     * @param executeResponse
     */
    private void oneJob(ExecuteResponse executeResponse) {
        if (StringUtils.isNullOrBlank(executeResponse.getExecMsg())) {
            executeResponse.setExecMsg("执行完毕.");
        }
        try {
            if (executeResponse.getJobId() != null) {
                subJobService.updateSubJobRespById(executeResponse);
            }
        } catch (Exception e) {
            logger.error("executeResp: ", e);
//            //todo 告警
//            CoeusMonitor.addException("subJobService executeResp error", e,
//                jsonMapper.toJson(executeResponse));
        }

        try {
            String execMsg = executeResponse.getExecMsg().length() > 500 ? executeResponse.getExecMsg().substring(0, 500) : executeResponse.getExecMsg();
            executeResponse.setExecMsg(execMsg);
            taskExecuteService.updateExecuteRespById(executeResponse);
        } catch (Exception e) {
            logger.error("executeResp: ", e);
//            //todo 告警
//            CoeusMonitor.addException("taskExecuteService executeResp error", e,
//                jsonMapper.toJson(executeResponse));
        }
    }

    /**
     * 多个job更新
     * @param executeResponse
     */
    private void multiJob(ExecuteResponse executeResponse) {
        try {
            if (executeResponse.getJobId() != null) {
                subJobService.updateSubJobRespById(executeResponse);
            }
        } catch (Exception e) {
            logger.error("subJobService executeResp error:{},executeResponse:{}", e.getMessage(),
                jsonMapper.toJson(executeResponse));
//            //todo 告警
//            CoeusMonitor.addException("subJobService executeResp error", e,
//                jsonMapper.toJson(executeResponse));
        }

        List<Integer> stateList = subJobService.findStateByTaskExecuteId(executeResponse
            .getTaskExecuteId());

        ExecuteResponse execRes = new ExecuteResponse();
        execRes.setTaskExecuteId(executeResponse.getTaskExecuteId());

        int stateSuccess = 0;
        int stateFail = 0;
        int stateTimeout = 0;
        int stateOther = 0;
        String execMsg = "";
        String msg = "成功{0}个, 失败{1}个, 超时{2}个, 等待结果{3}个";
        do {
            if (stateList == null || stateList.size() == 0) {
                if (Constants.EXEC_STATE_SUCCESS == executeResponse.getState().intValue()) {
                    execRes.setExecMsg("成功1个job,等待其它job结果.");
                    execRes.setEndTime(null);
                    execRes.setState(null);
                } else {
                    execRes.setExecMsg("失败1个job,等待其它job结果.");
                    execRes.setEndTime(executeResponse.getEndTime());
                    execRes.setState(executeResponse.getState());
                }
                break;
            }

            for (Integer state : stateList) {
                if (Constants.EXEC_STATE_SUCCESS == state.intValue()) {
                    stateSuccess++;
                    continue;
                }
                if (Constants.EXEC_STATE_FAIL == state.intValue()) {
                    stateFail++;
                    continue;
                }
                if (Constants.EXEC_STATE_TIMEOUT == state.intValue()) {
                    stateTimeout++;
                    continue;
                }
            }

            stateOther = executeResponse.getJobCount().intValue()
                         - (stateSuccess + stateFail + stateTimeout);
            if (stateFail > 0) {
                execRes.setState(Constants.EXEC_STATE_FAIL);
                break;
            }

            if (stateSuccess == executeResponse.getJobCount().intValue()) {
                execRes.setState(Constants.EXEC_STATE_SUCCESS);
                break;
            }

            if (stateOther > 0) {
                execRes.setState(null);
                break;
            }

            if (stateTimeout > 0) {
                execRes.setState(Constants.EXEC_STATE_TIMEOUT);
                break;
            }

        } while (false);

        execMsg = MessageFormat.format(msg, new Object[] { stateSuccess, stateFail, stateTimeout,
                stateOther });
        execRes.setExecMsg(execMsg);
        if (stateOther == 0) {
            execRes.setEndTime(executeResponse.getEndTime());
        }

        try {
            taskExecuteService.updateExecuteRespById(execRes);
        } catch (Exception e) {
            logger.error("taskExecuteService executeResp error:{},executeResponse:{}",
                e.getMessage(), jsonMapper.toJson(executeResponse));
//            //todo 告警
//            CoeusMonitor.addException("taskExecuteService executeResp error", e,
//                jsonMapper.toJson(executeResponse));
        }
    }

}
