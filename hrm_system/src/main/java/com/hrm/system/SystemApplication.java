package com.hrm.system;

import com.hrm.common.utils.IdWorker;
import com.hrm.common.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/8-18:40
 */
@SpringBootApplication(scanBasePackages = "com.hrm")
@EntityScan(value = "com.hrm.domain.system")
@Configuration()
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }

    @Bean
    public IdWorker getIdWorker() {
        return new IdWorker();
    }

//    @Bean()
//    public JwtUtils getJwtUtils1() {
//        final JwtUtils jwtUtils = new JwtUtils();
//        System.out.println("这是系统微服务的util");
//        System.out.println(jwtUtils);
//        return jwtUtils;
//    }

    /**
     * 解决no session问题
     * @return
     */
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter(){
        return new OpenEntityManagerInViewFilter();
    }

}
