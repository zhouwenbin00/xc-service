package com.zwb.demo.xc.api.media;

import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.media.response.CheckChunkResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/** Created by zwb on 2019/10/25 15:09 */
@Api(value = "媒资管理接口", description = "媒资管理接口，提供文件上传，文件处理等接口")
public interface MediaUploadControllerApi {

    @ApiOperation("文件上传注册")
    public ResponseResult register(
            String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

    @ApiOperation("分块检查")
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize);

    @ApiOperation("上传分块")
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5);

    @ApiOperation("合并文件")
    public ResponseResult mergechunks(
            String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);
}
