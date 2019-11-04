package com.zwb.demo.xc.order.dao;

import com.zwb.demo.xc.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/** Created by zwb on 2019/11/4 17:12 */
public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse, String> {
    // 根据用户和课程查询选课记录，用于判断是否添加选课
    XcLearningCourse findXcLearningCourseByUserIdAndCourseId(String userId, String courseId);
}
