package com.zwb.demo.xc.manage_media_process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/** Created by zwb on 2019/10/25 20:45 */
@SpringBootApplication
@EntityScan("com.zwb.demo.xc.domain.media") // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描framework中通用类
@ComponentScan(basePackages = {"com.zwb.demo.xc.manage_media_process"}) // 扫描本项目下的所有类
public class ManageMediaProcessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageMediaProcessApplication.class, args);
    }
}
