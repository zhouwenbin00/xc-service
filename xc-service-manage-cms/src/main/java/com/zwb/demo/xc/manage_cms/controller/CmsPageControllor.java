package com.zwb.demo.xc.manage_cms.controller;

import com.zwb.demo.xc.api.cms.CmsPageControllerApi;
import com.zwb.demo.xc.domain.cms.request.QueryPageRequest;
import com.zwb.demo.xc.manage_cms.service.PageService;
import com.zwb.demo.xc.model.response.QueryResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by zwb on 2019/9/27 18:59 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageControllor implements CmsPageControllerApi {

    @Autowired PageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            QueryPageRequest queryPageRequest) {
        /* QueryResult queryResult = new QueryResult();
        List<CmsPage> list = new ArrayList<>();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
        list.add(cmsPage);
        queryResult.setList(list);
        queryResult.setTotal(1);

        QueryResponseResult responseResult =
                new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return responseResult;*/
        return pageService.findList(page, size, queryPageRequest);
    }
}
