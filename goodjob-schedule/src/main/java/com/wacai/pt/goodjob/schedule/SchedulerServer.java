package com.wacai.pt.goodjob.schedule;

import com.wacai.pt.goodjob.common.exception.ScheduleRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by xuanwu on 16/3/22.
 */
@Component("schedulerServer")
public class SchedulerServer {
    private static Logger      logger = LoggerFactory.getLogger(SchedulerServer.class);

    @Autowired
    private SchedulerBoot      schedulerBoot;

    private Exception          initException;

    private Thread             startThread;

    @PostConstruct
    public synchronized void initiate() throws Throwable {
        if (initException != null) {
            throw initException;
        }
        try {
            logger.info("Initiate scheduler succeed.");
            start();
        } catch (Exception e) {
            logger.error("Initiate scheduler failed.", e);
            ScheduleRuntimeException excep = new ScheduleRuntimeException(
                "Initiate scheduler failed.", e);
            initException = excep;
            throw excep;
        }
    }

    public void start() throws Throwable {
        logger.info("Begin to start scheduler server.");
        startThread = new Thread() {
            public void run() {
                schedulerBoot.start();
            };
        };
        startThread.start();
        logger.info("Succeed to start scheduler server.");
    }

    public void stop() throws Throwable {
        logger.info("Begin to stop scheduler server.");
        try {
            schedulerBoot.stop();
            if (startThread != null) {
                logger.info("Wait scheduler start thread to terminate.");
                try {
                    startThread.join(2000);
                } catch (InterruptedException e) {
                }
                if (startThread.getState() != Thread.State.TERMINATED) {
                    startThread.interrupt();
                    try {
                        startThread.join(2000);
                    } catch (InterruptedException e) {
                    }
                }
                logger.info("SchedulerBoot start thread is terminated.");
            }
            logger.info("Succeed to stop scheduler server.");
        } catch (Exception e) {
            logger.warn("Failed to stop scheduler server.", e);
            throw e;
        }
    }
}
