package com.zwb.demo.xc.manage_course.controller;

import com.zwb.demo.xc.api.course.CategoryControllerApi;
import com.zwb.demo.xc.domain.course.ext.CategoryNode;
import com.zwb.demo.xc.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by zwb on 2019/10/15 11:18 */
@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {

    @Autowired CategoryService categoryService;

    @Override
    @GetMapping("/list")
    public CategoryNode findList() {
        return categoryService.findList();
    }
}
