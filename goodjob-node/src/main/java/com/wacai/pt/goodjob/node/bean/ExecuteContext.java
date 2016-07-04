package com.wacai.pt.goodjob.node.bean;

import com.wacai.pt.goodjob.remote.bean.ExecuteRequest;

/**
 * Created by xuanwu on 16/4/14.
 */
public class ExecuteContext {

    /**
     * 执行msg
     * 可以存放一些执行数据，会在控台显示
     */
    private String            execMsg;

    private Long              jobId;

    /**
     * 参数
     */
    private String            param;

    public ExecuteContext(){

    }

    public ExecuteContext(ExecuteRequest executeRequest){
        this.jobId = executeRequest.getJobId();
        this.param = executeRequest.getParam();
    }

    public String getExecMsg() {
        return execMsg;
    }

    public void setExecMsg(String execMsg) {
        this.execMsg = execMsg;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getParam() {
        return param;
    }
}
