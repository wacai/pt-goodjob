package com.wacai.pt.goodjob.node.service.impl;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.common.thread.GoodJobThreadpool;
import com.wacai.pt.goodjob.node.bean.ExecuteContext;
import com.wacai.pt.goodjob.node.file.FailHandle;
import com.wacai.pt.goodjob.remote.bean.ExecuteRequest;
import com.wacai.pt.goodjob.remote.bean.ExecuteResponse;
import com.wacai.pt.goodjob.remote.service.JobExecuteRespService;
import com.wacai.pt.goodjob.remote.service.JobExecuteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class AbstractJobExecuteService implements JobExecuteService {

    protected Logger                      logger            = LoggerFactory.getLogger(getClass());

    public final static GoodJobThreadpool executeThreadpool = new GoodJobThreadpool(
                                                                Constants.THREAD_EXECUTE_NAME, 50,
                                                                100,
                                                                new LinkedBlockingQueue<Runnable>(
                                                                    200));

    @Resource
    private FailHandle                    failHandle;

    @Resource
    private JobExecuteRespService         jobExecuteRespService;

    @Override
    public void execute(final ExecuteRequest request) {
        if (request.getJobId() == null) {
            logger.error("JobId is null.");
            return;
        }

        executeThreadpool.submit(new Runnable() {
            @Override
            public void run() {
                Throwable exception = null;
                long elapsed = 0L;
                ExecuteContext executeContext = new ExecuteContext(request);
                try {
                    long startTime = System.currentTimeMillis();
                    doExecute(executeContext);
                    elapsed = System.currentTimeMillis() - startTime;
                } catch (Throwable e) {
                    exception = e;
                    logger.error("business execute error: ", e);
                }

                request.setExecMsg(executeContext.getExecMsg());
                executeResponse(request, exception, elapsed);
            }
        });
    }

    private void executeResponse(final ExecuteRequest request, Throwable e, long elapsed) {
        ExecuteResponse executeResponse = new ExecuteResponse(request);
        executeResponse.setHostIp(NetUtils.getLocalHost());
        executeResponse.setEndTime(System.currentTimeMillis());
        executeResponse.setResponseTime(System.currentTimeMillis());
        executeResponse.setElapsed(elapsed);
        executeResponse.setState(Constants.EXEC_STATE_SUCCESS);
        executeResponse.setExecMsg(request.getExecMsg());
        if (e != null) {
            executeResponse.setExecMsg(e.getMessage());
            executeResponse.setState(Constants.EXEC_STATE_FAIL);
        }

        try {
            jobExecuteRespService.executeResp(executeResponse);
        } catch (Throwable rpcExcep) {
            logger.error("rpc request error: ", rpcExcep);
            failHandle.writerResponse(executeResponse);
        }
    }

    public abstract void doExecute(ExecuteContext executeContext) throws Throwable;
}
