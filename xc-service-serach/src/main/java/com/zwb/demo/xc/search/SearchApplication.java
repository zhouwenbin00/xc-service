package com.zwb.demo.xc.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/** Created by zwb on 2019/10/21 21:19 */
@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.zwb.demo.xc.domain.search") // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.search"}) // 扫描本项目下的所有类
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描common下的所有类
public class SearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
