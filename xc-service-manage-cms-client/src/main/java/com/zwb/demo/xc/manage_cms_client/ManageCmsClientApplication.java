package com.zwb.demo.xc.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/** Create by zwb on 2019-10-13 12:27 */
@SpringBootApplication
@EntityScan("com.zwb.demo.xc.domain.cms")
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描本common的所有类
@ComponentScan(basePackages = {"com.zwb.demo.xc.manage_cms_client"}) // 扫描本项目下的所有类
public class ManageCmsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class, args);
    }
}
