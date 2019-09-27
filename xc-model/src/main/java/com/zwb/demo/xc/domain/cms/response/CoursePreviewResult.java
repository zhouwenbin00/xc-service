package com.zwb.demo.xc.domain.cms.response;

import com.zwb.demo.xc.model.response.ResponseResult;
import com.zwb.demo.xc.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Created by admin on 2018/3/5. */
@Data
@ToString
@NoArgsConstructor
public class CoursePreviewResult extends ResponseResult {
    public CoursePreviewResult(ResultCode resultCode, String url) {
        super(resultCode);
        this.url = url;
    }

    String url;
}
