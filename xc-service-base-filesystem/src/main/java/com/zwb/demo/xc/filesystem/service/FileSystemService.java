package com.zwb.demo.xc.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.domain.filesystem.FileSystem;
import com.zwb.demo.xc.domain.filesystem.response.FileSystemCode;
import com.zwb.demo.xc.domain.filesystem.response.UploadFileResult;
import com.zwb.demo.xc.filesystem.dao.FileSystemRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/** Created by zwb on 2019/10/16 15:26 */
@Service
@Slf4j
public class FileSystemService {

    @Autowired FileSystemRepository fileSystemRepository;

    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;

    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;

    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;

    @Value("${xuecheng.fastdfs.charset}")
    String charset;

    private void initFdfsConfig() {
        try {
            ClientGlobal.initByTrackers(tracker_servers.trim());
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
        } catch (IOException | MyException e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_INITFILE);
        }
    }

    // 上传文件
    public UploadFileResult upload(
            MultipartFile multipartFile, String filetag, String businesskey, String metadata) {

        // 上传文件到fastdfs获得一个文件id
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        String fileId = fdfs_upload(multipartFile);
        // 将文件id和其他信息存入mongodb
        if (StringUtils.isBlank(fileId)) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        if (StringUtils.isNotBlank(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        // 名称
        fileSystem.setFileName(multipartFile.getOriginalFilename()); // 大小
        fileSystem.setFileSize(multipartFile.getSize()); // 文件类型
        fileSystem.setFileType(multipartFile.getContentType());
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    private String fdfs_upload(MultipartFile multipartFile) {

        try {
            initFdfsConfig();
            //
            // ClientGlobal.initByProperties("config/fastdfs-client.properties.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = storageClient1.upload_file1(bytes, extName, null);
            return fileId;
        } catch (IOException | MyException e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        return null;
    }
}
