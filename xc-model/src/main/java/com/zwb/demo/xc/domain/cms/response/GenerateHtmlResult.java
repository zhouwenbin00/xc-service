package com.zwb.demo.xc.domain.cms.response;

import com.zwb.demo.xc.model.response.ResponseResult;
import com.zwb.demo.xc.model.response.ResultCode;
import lombok.Data;

/**
 * Created by mrt on 2018/3/31.
 */
@Data
public class GenerateHtmlResult extends ResponseResult {
    String html;
    public GenerateHtmlResult(ResultCode resultCode, String html) {
        super(resultCode);
        this.html = html;
    }
}
