package com.zwb.demo.xc.ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/** Created by zwb on 2019/10/31 11:45 */
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.zwb.demo.xc.ucenter.dao")
@EntityScan("com.zwb.demo.xc.domain.ucenter") // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描common下的所有类
@SpringBootApplication
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class, args);
    }

    @Bean
    public RestTemplate template() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
