package com.wacai.pt.goodjob.schedule.util;

import com.wacai.pt.goodjob.schedule.service.HostService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuanwu on 16/5/11.
 */
public class HostsUtil {
    //key:taskId
    //禁用的host
    private static Map<String, List<String>>      disableHosts             = new ConcurrentHashMap<>();
    //key:taskId
    //指定为调度机
    private static Map<String, List<String>>      ownerHosts               = new ConcurrentHashMap<>();

    //key:taskId + "_" + ip
    private static Map<String, Integer>           ipsMap                   = new ConcurrentHashMap<String, Integer>(
                                                                               4096);

    private static final ScheduledExecutorService scheduledExecutorService = Executors
                                                                               .newScheduledThreadPool(1);
    private HostService                           hostService;

    private volatile static HostsUtil             singleInstance           = null;
    private final static Object                   obj                      = new Object();
    private final static Object                   obj1                     = new Object();

    private HostsUtil() {

    }

    public static HostsUtil getHostsUtil() {
        if (singleInstance == null) {
            synchronized (obj) {
                if (singleInstance == null) {
                    singleInstance = new HostsUtil();
                    singleInstance.init();
                }
            }
        }

        return singleInstance;
    }

    private void init() {
        try {
            //24小时执行一次
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    ipsMap.clear();
                }
            }, 0, 3, TimeUnit.DAYS);

            hostService = SpringContextHolder.getBean("hostService");
        } catch (Throwable t) {
            //ignore
        }

    }

    public Map<String, List<String>> getDisableHosts() {
        return disableHosts;
    }

    public void setDisableHosts(Map<String, List<String>> disableHosts) {
        HostsUtil.disableHosts = disableHosts;
    }

    public Map<String, List<String>> getOwnerHosts() {
        return ownerHosts;
    }

    public void setOwnerHosts(Map<String, List<String>> ownerHosts) {
        HostsUtil.ownerHosts = ownerHosts;
    }

    public void clearHosts() {
        disableHosts.clear();
        ownerHosts.clear();
    }

    public void ipCache(final String ip, final Integer taskId) {
        try {
            ThreadUtil.ignoreThreadpool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!ipsMap.containsKey(taskId + "_" + ip)) {
                            ipCheck(ip, taskId);
                        }else {
                            hostService.updateTimeById(ipsMap.get(taskId + "_" + ip));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //ignore
                    }
                }
            });
        } catch (Throwable t) {
            //ignore
        }
    }

    public void ipCheck(final String ip, final Integer taskId){
        Integer ipId = hostService.findIdByUnique(ip, taskId);
        if (ipId == null) {
            synchronized (obj1) {
                ipId = hostService.findIdByUnique(ip, taskId);
                if (ipId == null) {
                    ipId = hostService.insertIp(ip, taskId);
                }
            }
        } else {
            hostService.updateTimeById(ipId);
        }

        if (ipsMap.size() > 40960){
            ipsMap = new ConcurrentHashMap<String, Integer>(4096);
        }

        ipsMap.put(taskId + "_" + ip, ipId);
    }
}
