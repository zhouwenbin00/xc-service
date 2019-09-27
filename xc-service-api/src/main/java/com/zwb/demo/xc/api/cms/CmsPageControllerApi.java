package com.zwb.demo.xc.api.cms;

import com.zwb.demo.xc.domain.cms.request.QueryPageRequest;
import com.zwb.demo.xc.model.response.QueryResponseResult;
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
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
