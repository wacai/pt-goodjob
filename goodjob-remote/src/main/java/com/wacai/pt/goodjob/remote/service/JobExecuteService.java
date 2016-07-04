package com.wacai.pt.goodjob.remote.service;

import com.wacai.pt.goodjob.remote.bean.ExecuteRequest;

/**
 * Author: tianwen
 */
public interface JobExecuteService {

    void execute(ExecuteRequest request) throws Throwable;
}
