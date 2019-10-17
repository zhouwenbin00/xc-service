package com.zwb.demo.xc.manage_cms.controller;

import com.zwb.demo.xc.common.web.BaseController;
import com.zwb.demo.xc.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Create by zwb on 2019-10-06 20:00 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired PageService pageService;

    // 页面预览
    @RequestMapping(value = "/cms/preview/{pageId}", method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String pageId) throws IOException {
        // 执行静态化
        String pageHtml = pageService.getPageHtml(pageId);
        response.setHeader("Content-type", "text/html;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pageHtml.getBytes(StandardCharsets.UTF_8));
    }
}
