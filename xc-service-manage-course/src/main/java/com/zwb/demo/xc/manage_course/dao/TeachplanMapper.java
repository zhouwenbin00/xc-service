package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** Create by zwb on 2019-10-13 18:36 */
@Mapper
public interface TeachplanMapper {

    // 课程计划查询
    TeachplanNode selectList(@Param("courseId") String courseId);
}
