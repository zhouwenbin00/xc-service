package com.zwb.demo.xc.api.course;

import com.zwb.demo.xc.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** Created by zwb on 2019/10/15 11:13 */
@Api(
        value = "课程分类管理",
        description = "课程分类管理",
        tags = {"课程分类管理"})
public interface CategoryControllerApi {
    @ApiOperation("查询分类")
    CategoryNode findList();
}
