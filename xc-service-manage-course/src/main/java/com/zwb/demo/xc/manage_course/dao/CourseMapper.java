package com.zwb.demo.xc.manage_course.dao;

import com.github.pagehelper.Page;
import com.zwb.demo.xc.domain.course.CourseBase;
import com.zwb.demo.xc.domain.course.ext.CourseInfo;
import com.zwb.demo.xc.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/** Created by Administrator. */
@Mapper
public interface CourseMapper {
    CourseBase findCourseBaseById(String id);

    Page<CourseInfo> findCourseList(CourseListRequest courseListRequest);
}
