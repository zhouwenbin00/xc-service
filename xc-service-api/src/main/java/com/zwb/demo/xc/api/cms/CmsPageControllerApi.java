package com.zwb.demo.xc.api.cms;

import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.domain.cms.request.QueryPageRequest;
import com.zwb.demo.xc.domain.cms.response.CmsPageResult;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.cms.response.CmsPostPageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/** Created by zwb on 2019/9/27 18:29 */
@Api(value = "cms页面管理接口", description = "cms页面管理接口,提供界面的增删改查")
public interface CmsPageControllerApi {

    // 页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "page",
                value = "页码",
                required = true,
                paramType = "path",
                dataType = "int"),
        @ApiImplicitParam(
                name = "size",
                value = "每页记录数",
                required = true,
                paramType = "path",
                dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    // 新增页面
    @ApiOperation("新增页面")
    CmsPageResult addCmsPage(CmsPage cmsPage);

    @ApiOperation("根据页面id查询信息")
    CmsPage findById(String id);

    @ApiOperation("修改页面")
    CmsPageResult edit(String id, CmsPage cmsPage);

    @ApiOperation("删除页面")
    ResponseResult delete(String id);

    @ApiOperation("发布页面")
    ResponseResult post(String id);

    @ApiOperation("保存页面")
    CmsPageResult savePage(CmsPage cmsPage);

    @ApiOperation("一键发布页面")
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
