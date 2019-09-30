package com.zwb.demo.xc.manage_cms.service;

import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.domain.cms.request.QueryPageRequest;
import com.zwb.demo.xc.domain.cms.response.CmsCode;
import com.zwb.demo.xc.domain.cms.response.CmsPageResult;
import com.zwb.demo.xc.manage_cms.dao.CmsPageRepository;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** Created by zwb on 2019/9/27 20:01 */
@Service
public class PageService {

    @Autowired CmsPageRepository cmsPageRepository;

    /**
     * 页面查询方法
     *
     * @param page 页码，从1开始计数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        // 自定义条件查询
        // 定义一个条件匹配器
        ExampleMatcher exampleMatcher =
                ExampleMatcher.matching()
                        .withMatcher(
                                "pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 设置站点id
        if (StringUtils.isNotBlank(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        // 页面别名
        if (StringUtils.isNotBlank(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        // 分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent()); // 数据列表
        queryResult.setTotal(all.getTotalElements()); // 总记录数
        QueryResponseResult queryResponseResult =
                new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult addCmsPage(CmsPage cmsPage) {
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 校验唯一性
        CmsPage cmsPage1 =
                cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                        cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        // 调用dao，新增页面
        cmsPage.setPageId(null);
        CmsPage save = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, save);
    }

    public CmsPage findById(String id) {
        Optional<CmsPage> optinal = cmsPageRepository.findById(id);
        return optinal.orElse(null);
    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage one = findById(id);
        if (one != null) {
            one.setTemplateId(cmsPage.getTemplateId());
            one.setSiteId(cmsPage.getSiteId());
            one.setPageAliase(cmsPage.getPageAliase());
            one.setPageName(cmsPage.getPageName());
            one.setPageWebPath(cmsPage.getPageWebPath());
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS, one);
        }
        // 修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public ResponseResult delete(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            cmsPageRepository.delete(optional.get());
            return new ResponseResult(CommonCode.SUCCESS);
        }
        // 查询失败
        return new ResponseResult(CommonCode.FAIL);
    }
}
