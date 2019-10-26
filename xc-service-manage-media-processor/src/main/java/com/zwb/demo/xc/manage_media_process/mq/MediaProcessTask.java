package com.zwb.demo.xc.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.zwb.demo.xc.domain.media.MediaFile;
import com.zwb.demo.xc.domain.media.MediaFileProcess_m3u8;
import com.zwb.demo.xc.manage_media_process.dao.MediaFileRepository;
import com.zwb.demo.xc.utils.HlsVideoUtil;
import com.zwb.demo.xc.utils.Mp4VideoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Created by zwb on 2019/10/26 14:53 */
@Component
@Slf4j
public class MediaProcessTask {

    @Autowired MediaFileRepository mediaFileRepository;
    // ffmpeg绝对路径
    @Value("${xc-service-manage-media.ffmpeg-path}")
    String ffmpeg_path;
    // 上传文件根目录
    @Value("${xc-service-manage-media.upload-location}")
    String serverPath;

    @RabbitListener(
            queues = "${xc-service-manage-media.mq.queue-media-video-processor}",
            containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg) {
        Map msgMap = JSON.parseObject(msg, Map.class);
        log.info("receive media process task msg :{} ", msgMap);
        // 解析消息
        // 媒资文件id
        String mediaId = (String) msgMap.get("mediaId");
        // 获取媒资文件信息
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent()) {
            return;
        }
        MediaFile mediaFile = optional.get();
        String fileType = mediaFile.getFileType();
        if (!fileType.equals("avi")) {
            mediaFile.setProcessStatus("303004"); // 无需处理
            return;
        } else {
            mediaFile.setProcessStatus("303001"); // 处理中
        }
        mediaFileRepository.save(mediaFile);
        // 使用工具类将avi生成MP4
        String videPath = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
        // mp4文件名
        String mp4Name = mediaFile.getFileId() + ".mp4";
        // MP4文件路径
        String mp4FolderPath = serverPath + mediaFile.getFilePath();
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, videPath, mp4Name, mp4FolderPath);
        String result = mp4VideoUtil.generateMp4();
        if (result == null || !result.equals("success")) {
            mediaFile.setProcessStatus("303003");
            // 定义mediaFileProcess_m3u8
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            // 记录失败原因
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }

        // 使用工具类将mp4生成m3u8
        // mp4视频路径
        String mp4_video_path = serverPath + mediaFile.getFilePath() + mp4Name;
        // m3u8文件名次
        String m3u8_name = mediaFile.getFileId() + ".m3u8";
        // m3u8文所在在路径
        String m3u8_folder_path = serverPath + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil =
                new HlsVideoUtil(ffmpeg_path, mp4_video_path, m3u8_name, m3u8_folder_path);
        String ts_result = hlsVideoUtil.generateM3u8();
        if (ts_result == null || !ts_result.equals("success")) {
            mediaFile.setProcessStatus("303003");
            // 定义mediaFileProcess_m3u8
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            // 记录失败原因
            mediaFileProcess_m3u8.setErrormsg(ts_result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        mediaFile.setProcessStatus("303002");
        // 获取ts列表
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        // 定义mediaFileProcess_m3u8
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);

        // 保存文件url
        String file_url = mediaFile.getFilePath() + "hls/" + m3u8_name;
        mediaFile.setFileUrl(file_url);
        mediaFileRepository.save(mediaFile);
    }
}
