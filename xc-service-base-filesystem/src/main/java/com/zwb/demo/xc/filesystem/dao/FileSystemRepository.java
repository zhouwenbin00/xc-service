package com.zwb.demo.xc.filesystem.dao;

import com.zwb.demo.xc.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Created by zwb on 2019/10/16 15:26 */
public interface FileSystemRepository extends MongoRepository<FileSystem, String> {}
