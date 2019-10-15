package com.zwb.demo.xc.manage_cms.dao;

import com.zwb.demo.xc.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Created by zwb on 2019/10/15 11:30 */
public interface SysDictionaryDao extends MongoRepository<SysDictionary, String> {

    // 根据字典分类查询字典信息
    SysDictionary findBydType(String dType);
}
