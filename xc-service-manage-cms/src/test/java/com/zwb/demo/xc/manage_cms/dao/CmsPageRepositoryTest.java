package com.zwb.demo.xc.manage_cms.dao;

import com.zwb.demo.xc.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

/** Created by zwb on 2019/9/27 19:13 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CmsPageRepositoryTest {
    @Autowired CmsPageRepository cmsPageRepository;

    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    // 分页查询
    @Test
    public void testFindPage() {
        // 分页参数
        int page = 0; // 从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testFindPageByExample() {
        // 分页参数
        int page = 0; // 从0开始
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 要查询5a754adf6abb500ad05688d9
        //        cmsPage.setSiteId("5a754adf6abb500ad0568");
        // 模糊查询页面别名
        cmsPage.setPageAliase("轮播图");
        // 条件匹配起
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher =
                exampleMatcher.withMatcher(
                        "pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 定义Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all);
    }

    // 修改
    @Test
    public void testUpdate() {
        // 查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5a754adf6abb500ad05688d9");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageAliase("test01");
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }
    }

    // 根据页面名称查询
    @Test
    public void testFindByPageName() {
        CmsPage cmsPage = cmsPageRepository.findByPageName("index.html");
        System.out.println(cmsPage);
    }
}
