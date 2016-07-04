package com.wacai.pt.goodjob.schedule;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

/**
 * Created by xuanwu on 16/3/22.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class DubboWithSchdSbApplication {
    /**
     * SpringApplication的构造参数中， 标注了@SpringBootApplication的Main入口类是必须的；
     * 后面的参数一般传入dubbo服务的bean定义所在的配置文件路径，比如"classpath*:/spring/*.xml"
     * 之后，直接运行即可；（当然也可以设置一些参数，比如禁止banner打印等）
     */
    public static void main(String[] args) throws Throwable{
        System.setProperty("druid.logType", "slf4j");
        String classPath = "classpath*:META-INF/spring/Service-*.xml";
        SpringApplication application = new SpringApplication(DubboWithSchdSbApplication.class, classPath);
        application.run(args);

//        SchedulerServer schedulerServer = new SchedulerServer();
//        schedulerServer.initiate(classPath);
//        schedulerServer.start();
    }
}
