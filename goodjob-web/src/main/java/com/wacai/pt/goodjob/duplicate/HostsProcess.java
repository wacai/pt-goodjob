package com.wacai.pt.goodjob.duplicate;

import com.wacai.pt.goodjob.mapper.TaskHostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by xuanwu on 16/5/19.
 */
@Component("hostsProcess")
public class HostsProcess {
    private static final Logger logger     = LoggerFactory.getLogger(HostsProcess.class);
    @Resource
    private TaskHostMapper taskHostMapper;
    public HostsProcess(){
        Thread t = new Thread(new HostsProcess.ClearIp());
        t.setDaemon(true);
        t.start();
        logger.info("ClearIp Thread start.");
    }

    class ClearIp implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000 * 60 * 3L);
                    taskHostMapper.deleteExceedIps(new Date(System.currentTimeMillis() - 1000 * 60 * 10L));
                } catch (Throwable t) {
                    logger.error("clear ip failure:", t);
                }
            }
        }
    }
}
