package com.zwb.demo.xc.domain.cms.response;

import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.model.response.ResponseResult;
import com.zwb.demo.xc.model.response.ResultCode;
import lombok.Data;

/** Created by mrt on 2018/3/31. */
@Data
public class CmsPageResult extends ResponseResult {
    CmsPage cmsPage;

    public CmsPageResult(ResultCode resultCode, CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
}
