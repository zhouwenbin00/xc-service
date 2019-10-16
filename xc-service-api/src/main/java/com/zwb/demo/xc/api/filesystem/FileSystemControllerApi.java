package com.zwb.demo.xc.api.filesystem;

import com.zwb.demo.xc.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/** Created by zwb on 2019/10/16 15:23 */
@Api(value = "文件系统接口")
public interface FileSystemControllerApi {

    @ApiOperation("上传文件接口")
    UploadFileResult upload(
            MultipartFile multipartFile, String filetag, String businesskey, String metadata);
}
