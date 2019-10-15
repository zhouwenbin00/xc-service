package com.zwb.demo.xc.manage_cms.service;

import com.zwb.demo.xc.domain.system.SysDictionary;
import com.zwb.demo.xc.manage_cms.dao.SysDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by zwb on 2019/10/15 11:31 */
@Service
public class SysdictionaryService {
    @Autowired SysDictionaryDao sysDictionaryDao;
    // 根据字典分类type查询字典信息
    public SysDictionary findDictionaryByType(String type) {
        return sysDictionaryDao.findBydType(type);
    }
}
