package com.zwb.demo.xc.domain.cms.response;

import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.common.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Created by mrt on 2018/3/31. */
@Data
@NoArgsConstructor
public class CmsPageResult extends ResponseResult {
    CmsPage cmsPage;

    public CmsPageResult(ResultCode resultCode, CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
}
