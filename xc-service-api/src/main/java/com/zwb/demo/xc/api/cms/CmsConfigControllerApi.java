package com.zwb.demo.xc.api.cms;

import com.zwb.demo.xc.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** Create by zwb on 2019-10-06 17:25 */
@Api(value = "cms配置管理接口", description = "cms配置管理接口,提供数据模型的管理，查询接口")
public interface CmsConfigControllerApi {

    @ApiOperation("获取数据模型")
    CmsConfig getModel(String id);
}
