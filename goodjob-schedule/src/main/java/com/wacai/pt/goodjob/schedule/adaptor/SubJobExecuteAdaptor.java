package com.wacai.pt.goodjob.schedule.adaptor;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.wacai.pt.goodjob.interior.remote.service.ScheduleService;
import com.wacai.pt.goodjob.remote.service.JobExecuteRespService;
import com.wacai.pt.goodjob.remote.service.JobExecuteService;
import com.wacai.pt.goodjob.schedule.filter.dubbo.WacIpLoadBalance;
import com.wacai.pt.goodjob.schedule.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SubJobExecuteAdaptor {

    private static final Logger                                 logger             = LoggerFactory
                                                                                       .getLogger(SubJobExecuteAdaptor.class);

    private static ConcurrentHashMap<String, JobExecuteService> executeServiceMap  = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<Class, ServiceConfig>      providerServiceMap = new ConcurrentHashMap<>();

    private final static Object                                 obj                = new Object();

    private static ApplicationConfig                            application        = new ApplicationConfig();
    private static RegistryConfig                               registry           = new RegistryConfig();

    private static String                                       registryAddress;
    private static String                                       registryGroup;
    private static String                                       applicationName;

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @PostConstruct
    private void initDubbo() {
        application.setName(applicationName);

        // 连接注册中心配置
        registry.setAddress(registryAddress);
        registry.setProtocol("zookeeper");
        registry.setGroup(registryGroup);
        registry.setClient("zkclient");
    }

    public static void startDubboService() {
        createProvider(ScheduleService.class);
        createProvider(JobExecuteRespService.class);
    }

    private static <T> T createProvider(Class<T> clazz) {
        logger.info("----------createProvider--------------:{}", clazz.getName());
        T t = SpringContextHolder.getBean(clazz);
        if (null != providerServiceMap.get(clazz)) {
            return t;
        }

        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口
        // 服务提供者暴露服务配置
        ServiceConfig<T> service = new ServiceConfig<T>();
        service.setApplication(application);
        service.setRegistry(registry); // 多个注册中心可以用setRegistries()
        service.setInterface(clazz);
        service.setRef(t);

        // 暴露及注册服务
        service.export();
        providerServiceMap.put(clazz, service);

        return t;
    }

    public static JobExecuteService getJobExecuteService(String jobGroup){
        return getJobExecuteService(jobGroup, false);
    }

    public static JobExecuteService getJobExecuteService(String jobGroup, boolean isDetect) {
        JobExecuteService jobExecuteService = executeServiceMap.get(jobGroup);
        if (jobExecuteService == null) {
            synchronized (obj) {
                if (jobExecuteService == null) {
                    try {
                        ReferenceConfig<JobExecuteService> reference = new ReferenceConfig<JobExecuteService>();
                        reference.setApplication(application);
                        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
                        reference.setInterface(JobExecuteService.class);
                        reference.setGroup(jobGroup);
//                        reference.setAsync(true);
                        reference.setLoadbalance(WacIpLoadBalance.NAME);
                        reference.setRetries(0);
//                        reference.setSent(true);
                        reference.setCheck(false);

                        ReferenceConfigCache cache = ReferenceConfigCache.getCache();

                        //和本地bean一样使用xxxService
                        jobExecuteService = cache.get(reference);

                        executeServiceMap.put(jobGroup, jobExecuteService);
                        logger.info("put jobExecuteService reference, jobGroup:{}", jobGroup);
                    }catch (Throwable t){
                        jobExecuteService = null;
                        if (!isDetect) {
                            logger.warn(
                                    "Unexpected error occur at get {} jobExecuteService reference, cause:{} "
                                    , jobGroup, t);
                        }
                    }
                }
            }
        }

        return jobExecuteService;
    }

    public static void destroyConsumer() {
        logger.info("destroyConsumer ****************dubbo client destroyAll****************************");
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        for (Map.Entry<String, JobExecuteService> entry : executeServiceMap.entrySet()) {
            try {
                ReferenceConfig<JobExecuteService> reference = new ReferenceConfig<JobExecuteService>();
                reference.setInterface(JobExecuteService.class);
                reference.setGroup(entry.getKey());

                cache.destroy(reference);
            }catch (Throwable t){
                logger.error("****** destroy consumer error:", t);
            }
        }
        executeServiceMap.clear();
    }

    public static void destroyProvider() {
        logger.info("destroyConsumer ****************dubbo server unexport****************************");
        Set<Map.Entry<Class, ServiceConfig>> serviceConfigs = providerServiceMap.entrySet();
        for (Map.Entry<Class, ServiceConfig> serviceConfig : serviceConfigs) {
            serviceConfig.getValue().unexport();
        }
        providerServiceMap.clear();
    }

}
