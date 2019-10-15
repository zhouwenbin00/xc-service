package com.zwb.demo.xc.api.cms;

import com.zwb.demo.xc.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** Created by zwb on 2019/10/15 11:29 */
@Api(value = "数据字典接口", description = "提供数据字典接口的管理、查询功能")
public interface SysDicthinaryControllerApi {
    // 数据字典
    @ApiOperation(value = "数据字典查询接口")
    SysDictionary getByType(String type);
}
