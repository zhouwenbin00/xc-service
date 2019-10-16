package com.zwb.demo.xc.filesystem.controller;

import com.zwb.demo.xc.api.filesystem.FileSystemControllerApi;
import com.zwb.demo.xc.domain.filesystem.response.UploadFileResult;
import com.zwb.demo.xc.filesystem.service.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** Created by zwb on 2019/10/16 15:25 */
@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {

    @Autowired FileSystemService fileSystemService;

    @Override
    @PostMapping("/upload")
    public UploadFileResult upload(
            @RequestParam("multipartFile") MultipartFile multipartFile,
            @RequestParam("filetag") String filetag,
            @RequestParam(value = "businesskey", required = false) String businesskey,
            @RequestParam(value = "metadata", required = false) String metadata) {
        return fileSystemService.upload(multipartFile, filetag, businesskey, metadata);
    }
}
