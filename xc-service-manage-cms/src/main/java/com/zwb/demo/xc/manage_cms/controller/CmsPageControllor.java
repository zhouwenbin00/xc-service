package com.zwb.demo.xc.manage_cms.controller;

import com.zwb.demo.xc.api.cms.CmsPageControllerApi;
import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.domain.cms.request.QueryPageRequest;
import com.zwb.demo.xc.domain.cms.response.CmsPageResult;
import com.zwb.demo.xc.manage_cms.service.PageService;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Override
    @PostMapping("/add")
    public CmsPageResult addCmsPage(@RequestBody CmsPage cmsPage) {
        return pageService.addCmsPage(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.findById(id);
    }

    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.update(id, cmsPage);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }
}
