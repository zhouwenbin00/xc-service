package com.zwb.demo.xc.manage_cms_client.dao;

import com.zwb.demo.xc.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Create by zwb on 2019-10-13 12:28 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {}
