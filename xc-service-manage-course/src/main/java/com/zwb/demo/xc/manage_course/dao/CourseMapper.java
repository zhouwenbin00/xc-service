package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.CourseBase;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/** Created by Administrator. */
@Mapper
public interface CourseMapper {
    CourseBase findCourseBaseById(String id);
}
