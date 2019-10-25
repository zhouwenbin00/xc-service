package com.zwb.demo.xc.manage_media.service;

import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.domain.media.MediaFile;
import com.zwb.demo.xc.domain.media.response.CheckChunkResult;
import com.zwb.demo.xc.domain.media.response.MediaCode;
import com.zwb.demo.xc.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/** Created by zwb on 2019/10/25 15:14 */
@Service
public class MediaService {

    @Autowired MediaFileRepository mediaFileRepository;

    // 上传文件根目录
    @Value("${xc-service-manage-media.upload-location}")
    String uploadPath;

    /**
     * 根据文件md5得到文件路径 规则： 一级目录：md5的第一个字符 二级目录：md5的第二个字符 三级目录：md5 文件名：md5+文件扩展名
     *
     * @param fileMd5 文件md5值
     * @param fileExt 文件扩展名
     * @return 文件路径
     */
    public ResponseResult register(
            String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 检查文件在磁盘上是否存在
        // 文件所属目录的路径
        String fileFolderPath = getFileFolderPath(fileMd5);
        // 文件路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        // 文件是否存在
        boolean exists = file.exists();
        // 文件在mongodb中是否存在
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        if (exists && optional.isPresent()) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        // 文件不存在时，做一些准备工作
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            file.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 得到文件所属目录路径
     *
     * @param fileMd5
     * @return
     */
    private String getFileFolderPath(String fileMd5) {
        return uploadPath
                + fileMd5.substring(0, 1)
                + "/"
                + fileMd5.substring(1, 2)
                + "/"
                + fileMd5
                + "/";
    }

    // 得到文件路径
    private String getFilePath(String fileMd5, String fileExt) {
        return getFileFolderPath(fileMd5) + fileMd5 + "." + fileExt;
    }

    /**
     * 检查分块文件是否存在
     *
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        // 得到块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 快文件
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()) {
            return new CheckChunkResult(CommonCode.SUCCESS, true);
        }
        return new CheckChunkResult(CommonCode.SUCCESS, false);
    }

    // 得到块文件目录
    private String getChunkFileFolderPath(String fileMd5) {
        return getFileFolderPath(fileMd5) + "chunk/";
    }

    /**
     * 上传块文件
     *
     * @param file
     * @param chunk
     * @param fileMd5
     * @return
     */
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        // 块文件目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        // 块文件路径
        String chunkFilePath = chunkFileFolderPath + chunk;
        // 块文件目录
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            // 创建
            chunkFileFolder.mkdirs();
        }
        // 得到上传文件的输入流
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(new File(chunkFilePath));
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 合并文件
     *
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    public ResponseResult mergechunks(
            String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        // 合并所有分块
        // 分块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        File[] files = chunkFileFolder.listFiles();
        // 创建一个合并文件
        String filePath = getFilePath(fileMd5, fileExt);
        File mergeFile = new File(filePath);
        // 执行合并
        List<File> chunkFileList = Arrays.asList(files);
        mergeFile = mergeFile(chunkFileList, mergeFile);
        if (mergeFile == null) {
            // 合并失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        // 校验md5
        boolean checkFileMd5 = checkFileMd5(mergeFile, fileMd5);
        if (!checkFileMd5) {
            // 校验失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        // 写入信息
        // 将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();

        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        mediaFile.setFileOriginalName(fileName);
        // 文件路径保存相对路径
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5, fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private String getFileFolderRelativePath(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1)
                + "/"
                + fileMd5.substring(1, 2)
                + "/"
                + fileMd5
                + "/"
                + fileMd5
                + "."
                + fileExt;
    }

    private boolean checkFileMd5(File mergeFile, String md5) {
        try {
            // 创建文件输入流
            FileInputStream fileInputStream = new FileInputStream(mergeFile);
            // 得到md5
            String md5Hex = DigestUtils.md5Hex(fileInputStream);
            if (md5.equalsIgnoreCase(md5Hex)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /** 合并文件 */
    private File mergeFile(List<File> chunkFileList, File mergeFile) {
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        chunkFileList.sort(
                new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
                    }
                });
        try {
            mergeFile.createNewFile();
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
            byte[] b = new byte[1024];
            for (File chunkFile : chunkFileList) {
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                while ((len = raf_read.read(b)) != -1) {
                    raf_write.write(b, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
            return mergeFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
