package com.zwb.demo.xc.manage_media.controller;

import com.zwb.demo.xc.api.media.MediaUploadControllerApi;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.media.response.CheckChunkResult;
import com.zwb.demo.xc.manage_media.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Created by zwb on 2019/10/25 15:10 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired MediaService mediaService;

    @Override
    @PostMapping("/register")
    public ResponseResult register(
            String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return mediaService.register(fileMd5, fileName, fileSize, mimetype, fileExt);
    }

    @Override
    @PostMapping("/checkchunk")
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return mediaService.checkchunk(fileMd5, chunk, chunkSize);
    }

    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        return mediaService.uploadchunk(file, chunk, fileMd5);
    }

    @Override
    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(
            String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return mediaService.mergechunks(fileMd5, fileName, fileSize, mimetype, fileExt);
    }
}
