package com.wacai.pt.goodjob;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by xuanwu on 16/3/25.
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty("druid.logType", "slf4j");
        SpringApplication app = new SpringApplication(Main.class,"classpath*:/spring/*.xml");
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
