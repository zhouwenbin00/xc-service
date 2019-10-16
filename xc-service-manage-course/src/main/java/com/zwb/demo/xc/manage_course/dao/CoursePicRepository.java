package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/** Created by zwb on 2019/10/16 17:19 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {
    long deleteByCourseid(String courseid);
}
