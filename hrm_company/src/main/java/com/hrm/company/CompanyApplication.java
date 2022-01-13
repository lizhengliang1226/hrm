package com.hrm.company;

import com.hrm.common.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description 企业微服务
 * @Author LZL
 * @Date 2022/1/12-9:36
 */
@SpringBootApplication(scanBasePackages = "com.hrm")
//jpa注解的扫描
@EntityScan(value = "com.hrm.domain.company")
@EnableSwagger2
public class CompanyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class, args);
    }

    @Bean
    public IdWorker getIdWorker() {
        return new IdWorker();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("企业管理微服务API").description("企业管理CRUD").version("1.0").build();
    }
}
