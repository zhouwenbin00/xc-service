package com.zwb.demo.xc.domain.media.response;

import com.zwb.demo.xc.domain.media.MediaFile;
import com.zwb.demo.xc.domain.media.MediaVideoCourse;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.common.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Created by admin on 2018/3/5. */
@Data
@ToString
@NoArgsConstructor
public class MediaCourseResult extends ResponseResult {
    public MediaCourseResult(ResultCode resultCode, MediaVideoCourse mediaVideoCourse) {
        super(resultCode);
        this.mediaVideoCourse = mediaVideoCourse;
    }

    MediaFile mediaVideo;
    MediaVideoCourse mediaVideoCourse;
}
