package com.zwb.demo.xc.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/** Created by zwb on 2019/9/27 18:51 */
@SpringBootApplication
@EntityScan("com.zwb.demo.xc.domain.cms") // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.manage_cms"}) // 扫描本项目下的所有类
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描本common的所有类
public class ManageCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class, args);
    }
}
