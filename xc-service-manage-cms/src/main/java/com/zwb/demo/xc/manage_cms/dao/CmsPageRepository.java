package com.zwb.demo.xc.manage_cms.dao;

import com.zwb.demo.xc.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Created by zwb on 2019/9/27 19:12 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {

    CmsPage findByPageName(String pageName);
    // 根据页面名称，站点id，页面webpath查询
    CmsPage findByPageNameAndSiteIdAndPageWebPath(
            String pageName, String siteId, String pageWebPath);
}
