package com.zwb.demo.xc.manage_course.client;

import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.domain.cms.response.CmsPageResult;
import com.zwb.demo.xc.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** Created by zwb on 2019/10/17 16:57 */
@FeignClient(value = "xc-service-manager-cms") // 指定远程调用的服务名
public interface CmsPageClient {

    // 根据页面id查询页面信息
    @GetMapping("/cms/page/get/{id}") // GetMapping表示远程调用的http方法
    public CmsPage findCmsPageById(@PathVariable("id") String id);

    @PostMapping("/cms/page/save")
    CmsPageResult save(@RequestBody CmsPage cmsPage);

    // 一键发布接口
    @PostMapping("/cms/page/postPageQuick")
    CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}
