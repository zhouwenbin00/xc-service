package com.zwb.demo.xc.manage_media.controller;

import com.zwb.demo.xc.api.media.MediaFileControllerApi;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.domain.media.request.QueryMediaFileRequest;
import com.zwb.demo.xc.manage_media.service.MediaFileServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by zwb on 2019/10/26 16:29 */
@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {

    @Autowired MediaFileServce mediaFileServce;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            QueryMediaFileRequest queryMediaFileRequest) {
        return mediaFileServce.findList(page, size, queryMediaFileRequest);
    }
}
