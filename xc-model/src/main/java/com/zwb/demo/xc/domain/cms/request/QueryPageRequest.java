package com.zwb.demo.xc.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** Created by root on 2019/9/27 17:58 */
@Data
public class QueryPageRequest {
    // 站点id
    @ApiModelProperty("站点id")
    private String siteId;
    // 界面id
    @ApiModelProperty("界面id")
    private String pageId;
    // 页面名称
    @ApiModelProperty("页面名称")
    private String pageName;
    // 别名
    @ApiModelProperty("别名")
    private String pageAliase;
    // 模板id
    @ApiModelProperty("模板id")
    private String tempateId;
    // ...
}
