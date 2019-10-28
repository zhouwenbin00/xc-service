package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/** Created by zwb on 2019/10/28 16:17 */
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub, String> {

    // 根据课程id删除
    long deleteByCourseId(String courseId);
}
