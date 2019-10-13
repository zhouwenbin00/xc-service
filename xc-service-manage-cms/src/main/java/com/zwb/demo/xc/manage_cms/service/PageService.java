package com.zwb.demo.xc.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.domain.cms.CmsConfig;
import com.zwb.demo.xc.domain.cms.CmsPage;
import com.zwb.demo.xc.domain.cms.CmsTemplate;
import com.zwb.demo.xc.domain.cms.request.QueryPageRequest;
import com.zwb.demo.xc.domain.cms.response.CmsCode;
import com.zwb.demo.xc.domain.cms.response.CmsPageResult;
import com.zwb.demo.xc.manage_cms.config.RabbitmqConfig;
import com.zwb.demo.xc.manage_cms.dao.CmsConfigRepository;
import com.zwb.demo.xc.manage_cms.dao.CmsPageRepository;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.common.model.response.ResponseResult;
import com.zwb.demo.xc.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.CMSConfig;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Created by zwb on 2019/9/27 20:01 */
@Service
@Slf4j
public class PageService {

    @Autowired CmsPageRepository cmsPageRepository;
    @Autowired CmsConfigRepository cmsConfigRepository;
    @Autowired CmsTemplateRepository cmsTemplateRepository;
    @Autowired RestTemplate restTemplate;
    @Autowired GridFSBucket gridFSBucket;
    @Autowired GridFsTemplate gridFsTemplate;
    @Autowired RabbitTemplate rabbitTemplate;

    /**
     * 页面查询方法
     *
     * @param page 页码，从1开始计数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        // 自定义条件查询
        // 定义一个条件匹配器
        ExampleMatcher exampleMatcher =
                ExampleMatcher.matching()
                        .withMatcher(
                                "pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 条件值对象
        CmsPage cmsPage = new CmsPage();
        // 设置站点id
        if (StringUtils.isNotBlank(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        // 页面别名
        if (StringUtils.isNotBlank(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        // 分页参数
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        page = page - 1;
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent()); // 数据列表
        queryResult.setTotal(all.getTotalElements()); // 总记录数
        QueryResponseResult queryResponseResult =
                new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    /**
     * 新增页面
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult addCmsPage(CmsPage cmsPage) {
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        // 校验唯一性
        CmsPage cmsPage1 =
                cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                        cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        // 调用dao，新增页面
        cmsPage.setPageId(null);
        CmsPage save = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, save);
    }

    public CmsPage findById(String id) {
        Optional<CmsPage> optinal = cmsPageRepository.findById(id);
        return optinal.orElse(null);
    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage one = findById(id);
        if (one != null) {
            one.setTemplateId(cmsPage.getTemplateId());
            one.setSiteId(cmsPage.getSiteId());
            one.setPageAliase(cmsPage.getPageAliase());
            one.setPageName(cmsPage.getPageName());
            one.setPageWebPath(cmsPage.getPageWebPath());
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            one.setDataUrl(cmsPage.getDataUrl());
            cmsPageRepository.save(one);
            return new CmsPageResult(CommonCode.SUCCESS, one);
        }
        // 修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public ResponseResult delete(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            cmsPageRepository.delete(optional.get());
            return new ResponseResult(CommonCode.SUCCESS);
        }
        // 查询失败
        return new ResponseResult(CommonCode.FAIL);
    }

    /** 根据id查询config */
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        return optional.orElse(null);
    }

    /** 页面静态化的方法 */
    public String getPageHtml(String pageId) {
        // 获取dataUrl

        // 获取数据模型
        Map model = getModelByPageId(pageId);
        if (model == null) {
            // 数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        // 获取模板信息
        String template = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            // 模板id为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 执行页面静态化
        return generationHtml(template, model);
    }

    /** 生成html */
    private String generationHtml(String templateContent, Map model) {
        // 创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateContent);
        // 向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            // 获取模板
            Template template = configuration.getTemplate("template");
            // 调用api执行静态化
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 获取页面模板 */
    private String getTemplateByPageId(String pageId) {
        // 获取页面信息
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXIST);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            // 模板id为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        // 查询模板信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        String content = null;
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            String templateFileId = cmsTemplate.getTemplateFileId();
            if (StringUtils.isEmpty(templateFileId)) {
                // 模板文件id为空
                ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_NOTEXIST);
            }
            // 查询文件
            GridFSFile gridFSFile =
                    gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            // 打开下载流
            GridFSDownloadStream gridFSDownloadStream =
                    gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            // 创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            // 从流中获取数据

            try {
                content = IOUtils.toString(gridFsResource.getInputStream(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /** 获取数据模型 */
    private Map getModelByPageId(String pageId) {
        // 获取页面信息
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {
            // 页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXIST);
        }
        // 获取dataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            // 页面的dataUrl为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        // 通过restTemplate请求url获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        return forEntity.getBody();
    }

    /** 发布页面 */
    public ResponseResult post(String pageId) {
        // 执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        // 存储GridFs
        saveHtml(pageId, pageHtml);
        // 向mq发消息
        sendPostPage(pageId);
        log.info("post success");
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /** 向mq发送消息 */
    private void sendPostPage(String pageId) {
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALLD_PARAM);
        }
        // 创建消息对象
        Map<String, String> msg = new HashMap<>();
        msg.put("pageId", pageId);
        String jsonString = JSON.toJSONString(msg);
        // 站点id
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, jsonString);
    }

    /** 保存html到GridFS */
    private CmsPage saveHtml(String pageId, String htmlContent) {
        // 先得到页面信息
        CmsPage cmsPage = this.findById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALLD_PARAM);
        }
        ObjectId objectId = null;
        // 将文件内容转成输入流
        try {
            InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
            // 将文件内容保存到GridFs
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将html文件id更新到cmsPage
        cmsPage.setHtmlFileId(objectId.toHexString());
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }
}
