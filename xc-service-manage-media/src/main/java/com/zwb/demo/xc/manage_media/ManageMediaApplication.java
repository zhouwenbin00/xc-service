package com.zwb.demo.xc.manage_media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/** Created by zwb on 2019/10/24 20:48 */
@SpringBootApplication
@EnableDiscoveryClient // 从Eureka Server获取服务
@EntityScan("com.xuecheng.framework.domain.media") // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描framework中通用类
@ComponentScan(basePackages = {"com.zwb.demo.xc.manage_media"}) // 扫描本项目下的所有类
public class ManageMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageMediaApplication.class, args);
    }
}
