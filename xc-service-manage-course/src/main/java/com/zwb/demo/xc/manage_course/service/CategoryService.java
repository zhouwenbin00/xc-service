package com.zwb.demo.xc.manage_course.service;

import com.zwb.demo.xc.domain.course.ext.CategoryNode;
import com.zwb.demo.xc.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Created by zwb on 2019/10/15 11:18 */
@Service
public class CategoryService {
    @Autowired CategoryMapper categoryMapper;
    // 查询分类
    public CategoryNode findList() {
        return categoryMapper.selectList();
    }
}
