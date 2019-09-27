package com.zwb.demo.xc.domain.filesystem.response;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.zwb.demo.xc.model.response.ResponseResult;
import com.zwb.demo.xc.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/** Created by admin on 2018/3/5. */
@Data
@ToString
public class UploadFileResult extends ResponseResult {
    @ApiModelProperty(value = "文件信息", example = "true", required = true)
    FileSystem fileSystem;

    public UploadFileResult(ResultCode resultCode, FileSystem fileSystem) {
        super(resultCode);
        this.fileSystem = fileSystem;
    }
}
