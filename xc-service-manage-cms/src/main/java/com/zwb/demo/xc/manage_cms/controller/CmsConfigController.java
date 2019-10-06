package com.zwb.demo.xc.manage_cms.controller;

import com.zwb.demo.xc.api.cms.CmsConfigControllerApi;
import com.zwb.demo.xc.domain.cms.CmsConfig;
import com.zwb.demo.xc.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Create by zwb on 2019-10-06 17:30 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {
    @Autowired PageService pageService;

    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        return pageService.getConfigById(id);
    }
}
