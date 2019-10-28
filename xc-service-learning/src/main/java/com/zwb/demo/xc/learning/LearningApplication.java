package com.zwb.demo.xc.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/** Created by zwb on 2019/10/28 17:50 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EntityScan(value = {"com.zwb.demo.xc.domain.learning", "com.zwb.demo.xc.domain.task"}) // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.learning"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描common下的所有类
public class LearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningApplication.class, args);
    }
}
