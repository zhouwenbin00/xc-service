package com.zwb.demo.xc.manage_media.dao;

import com.zwb.demo.xc.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MediaFileRepository extends MongoRepository<MediaFile, String> {}
