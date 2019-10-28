package com.zwb.demo.xc.api.learning;

import com.zwb.demo.xc.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** Created by zwb on 2019/10/28 17:53 */
@Api(value = "录播课程学习管理", description = "录播课程学习管理")
public interface CourseLearningControllerApi {

    @ApiOperation("获取课程学习地址")
    public GetMediaResult getmedia(String courseId, String teachplanId);
}
