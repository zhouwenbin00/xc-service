package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.TeachplanMedia;
import com.zwb.demo.xc.domain.media.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {
    // 根据课程id查询列表
    List<TeachplanMedia> findByCourseId(String courseid);
}
