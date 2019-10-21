package com.zwb.demo.xc.manage_cms.dao;

import com.zwb.demo.xc.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Created by zwb on 2019/10/21 13:50 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {}
