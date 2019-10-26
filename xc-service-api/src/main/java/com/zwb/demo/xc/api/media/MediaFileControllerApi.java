package com.zwb.demo.xc.api.media;

import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.domain.media.request.QueryMediaFileRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** Created by zwb on 2019/10/26 16:27 */
@Api(
        value = "媒体文件管理",
        description = "媒体文件管理接口",
        tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {

    @ApiOperation("查询文件列表")
    public QueryResponseResult findList(
            int page, int size, QueryMediaFileRequest queryMediaFileRequest);
}
