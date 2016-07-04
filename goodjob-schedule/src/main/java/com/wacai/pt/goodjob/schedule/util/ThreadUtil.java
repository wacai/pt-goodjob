package com.wacai.pt.goodjob.schedule.util;

import com.wacai.pt.goodjob.common.Constants;
import com.wacai.pt.goodjob.common.thread.GoodJobThreadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by xuanwu on 16/4/10.
 */
public class ThreadUtil {
    public final static GoodJobThreadpool ignoreThreadpool = new GoodJobThreadpool(
                                                               Constants.THREAD_GROUP_NAME,
                                                               50,
                                                               50,
                                                               new LinkedBlockingQueue<Runnable>(
                                                                   5000),
                                                               new ThreadPoolExecutor.DiscardOldestPolicy());

    public final static GoodJobThreadpool schedThreadpool  = new GoodJobThreadpool(
                                                               Constants.THREAD_GROUP_NAME,
                                                               120,
                                                               500,
                                                               new LinkedBlockingQueue<Runnable>(10),
                                                               new ThreadPoolExecutor.DiscardOldestPolicy());
}
