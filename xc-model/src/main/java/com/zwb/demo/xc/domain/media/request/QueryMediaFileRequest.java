package com.zwb.demo.xc.domain.media.request;

import com.zwb.demo.xc.common.model.request.RequestData;
import lombok.Data;

@Data
public class QueryMediaFileRequest extends RequestData {

    private String fileOriginalName;
    private String processStatus;
    private String tag;
}
