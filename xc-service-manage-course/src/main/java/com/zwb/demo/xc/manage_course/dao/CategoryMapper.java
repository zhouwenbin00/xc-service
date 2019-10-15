package com.zwb.demo.xc.manage_course.dao;

import com.zwb.demo.xc.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/** Created by zwb on 2019/10/15 11:13 */
@Mapper
public interface CategoryMapper {
    // 查询分类
    public CategoryNode selectList();
}
