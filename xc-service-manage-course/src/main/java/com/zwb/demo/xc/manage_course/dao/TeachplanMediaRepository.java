package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.TeachplanMedia;
import com.zwb.demo.xc.domain.media.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {}
