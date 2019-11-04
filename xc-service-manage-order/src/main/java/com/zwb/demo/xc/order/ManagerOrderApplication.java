package com.zwb.demo.xc.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/** Created by zwb on 2019/11/4 14:02 */
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(value = {"com.zwb.demo.xc.domain.order", "com.zwb.demo.xc.domain.task"}) // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描framework中通用类
@ComponentScan(basePackages = {"com.zwb.demo.xc.order"}) // 扫描本项目下的所有类
@SpringBootApplication
public class ManagerOrderApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManagerOrderApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
