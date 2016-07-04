package com.wacai.pt.goodjob.example;

import com.wacai.pt.goodjob.common.utils.JsonMapper;
import com.wacai.pt.goodjob.node.bean.ExecuteContext;
import com.wacai.pt.goodjob.node.service.impl.AbstractJobExecuteService;

/**
 * Author: bailian Date : 2015/10/30
 */
public class JobExecuteServiceException3 extends AbstractJobExecuteService {

  /**
   * 如果是分片任务请从executeContext 获取param参数
   * 一般原则param为json
   * @param executeContext
   * @throws Throwable
   */
  @Override
  public void doExecute(ExecuteContext executeContext) throws Throwable {
    //这里触发你的job, 如果有异常抛出，会被认为job执行失败
    //executeContext.execMsg 可以存放业务执行数据，限制4000长度，会在控台显示，如果这个方法抛异常显示就以异常为主。
    // 这里执行你的方法...


    logger.info("============executeContext==========:{}", JsonMapper.getNonNullBinder().toJson(executeContext));
    Thread.sleep(30 * 60 * 1000);
    logger.info("JobExecuteServiceException3 doExecute!");
    throw new Exception("业务执行失败!");
  }
}
