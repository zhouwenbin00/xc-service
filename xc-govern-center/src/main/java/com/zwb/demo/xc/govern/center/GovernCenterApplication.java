package com.zwb.demo.xc.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/** Created by zwb on 2019/10/17 10:40 */
@SpringBootApplication
@EnableEurekaServer // 标识这是一个Eureka服务
public class GovernCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernCenterApplication.class, args);
    }
}
