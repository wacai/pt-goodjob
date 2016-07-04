package com.wacai.pt.goodjob.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xuanwu on 16/4/19.
 */
@Configuration
public class DruidStatView {

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
    }
}
