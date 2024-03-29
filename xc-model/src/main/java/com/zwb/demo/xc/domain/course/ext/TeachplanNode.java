package com.zwb.demo.xc.domain.course.ext;

import com.zwb.demo.xc.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/** Created by admin on 2018/2/7. */
@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

    // 媒资信息
    private String mediaId;
    private String mediaFileOriginalName;
}
