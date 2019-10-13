package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Created by Administrator. */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {

    List<Teachplan> findByCourseidAndAndParentid(String courseid, String parentid);
}
