package com.zwb.demo.xc.manage_course;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator
 * @version 1.0
 */
@SpringBootApplication
@MapperScan("com.zwb.demo.xc.manage_course.dao")
@EntityScan("com.zwb.demo.xc.domain") // 扫描实体类
@ComponentScan(basePackages = {"com.zwb.demo.xc.common"}) // 扫描common下的所有类
@ComponentScan(basePackages = {"com.zwb.demo.xc.api"}) // 扫描接口
@ComponentScan(basePackages = {"com.zwb.demo.xc.manage_course"})
public class ManageCourseApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageCourseApplication.class, args);
    }
}
