package com.zwb.demo.xc.manage_cms.controller;

import com.zwb.demo.xc.api.cms.SysDicthinaryControllerApi;
import com.zwb.demo.xc.domain.system.SysDictionary;
import com.zwb.demo.xc.manage_cms.service.SysdictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by zwb on 2019/10/15 11:32 */
@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDicthinaryControllerApi {
    @Autowired SysdictionaryService sysdictionaryService;

    // 根据字典分类id查询字典信息
    @Override
    @GetMapping("/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysdictionaryService.findDictionaryByType(type);
    }
}
