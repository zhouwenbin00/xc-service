package com.zwb.demo.xc.api.ucenter;

import com.zwb.demo.xc.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;

/** Created by zwb on 2019/10/31 11:52 */
@Api(value = "用户中心", description = "用户中心管理")
public interface UcenterControllerApi {

    public XcUserExt getUserext(String username);
}
