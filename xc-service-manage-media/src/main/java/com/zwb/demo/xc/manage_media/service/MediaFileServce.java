package com.zwb.demo.xc.manage_media.service;

import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.domain.media.MediaFile;
import com.zwb.demo.xc.domain.media.request.QueryMediaFileRequest;
import com.zwb.demo.xc.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/** Created by zwb on 2019/10/26 16:30 */
@Service
public class MediaFileServce {
    @Autowired MediaFileRepository mediaFileRepository;

    public QueryResponseResult findList(
            int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        if (queryMediaFileRequest == null) {
            queryMediaFileRequest = new QueryMediaFileRequest();
        }

        // 条件值对象
        MediaFile mediaFile = new MediaFile();
        // 条件匹配器
        ExampleMatcher exampleMatcher =
                ExampleMatcher.matching()
                        .withMatcher(
                                "tag",
                                ExampleMatcher.GenericPropertyMatchers.contains()) // tag字段 模糊匹配
                        .withMatcher(
                                "fileOriginalName",
                                ExampleMatcher.GenericPropertyMatchers.contains()) // 文件原始名称模糊匹配
                        .withMatcher(
                                "processStatus",
                                ExampleMatcher.GenericPropertyMatchers.exact()); // 处理状态精确匹配（默认）;
        // 查询条件对象
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }

        // 定义条件类型
        Example<MediaFile> example = Example.of(mediaFile, exampleMatcher);
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }

        page = page - 1;
        // 分页参数
        Pageable pageable = new PageRequest(page, size);
        // 分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        QueryResult<MediaFile> mediaFileQueryResult = new QueryResult<MediaFile>();
        mediaFileQueryResult.setList(all.getContent());
        mediaFileQueryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult<>(CommonCode.SUCCESS, mediaFileQueryResult);
    }
}
