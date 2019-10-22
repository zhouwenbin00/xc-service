package com.zwb.demo.xc.search.service;

import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.domain.course.CoursePub;
import com.zwb.demo.xc.domain.search.CourseSearchParam;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Created by zwb on 2019/10/22 17:44 */
@Service
public class EsCourseService {

    @Value("${xuecheng.elasticsearch.course.index}")
    private String es_index;

    @Value("${xuecheng.elasticsearch.course.type}")
    private String es_type;

    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;

    @Autowired RestHighLevelClient client;

    /** 课程搜索 */
    public QueryResponseResult<CoursePub> list(
            int page, int size, CourseSearchParam courseSearchParam) {
        // 创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(es_index);
        // 设置搜索时类型
        searchRequest.types(es_type);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 过滤字段
        String[] source_field_array = source_field.split(",");
        searchSourceBuilder.fetchSource(source_field_array, new String[] {});

        // 创建bool查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 搜索条件
        // 根据关键搜索-------------------------------------------------
        if (StringUtils.isNotBlank(courseSearchParam.getKeyword())) {
            // 匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder =
                    QueryBuilders.multiMatchQuery(
                            courseSearchParam.getKeyword(), "name", "teachplan", "description");
            // 设置匹配占比
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            // 提升另个字段的Boost值
            multiMatchQueryBuilder.field("name", 10);

            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        // 根据分类查询------------------------------------------------
        if (StringUtils.isNotBlank(courseSearchParam.getMt())) {
            // 一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotBlank(courseSearchParam.getSt())) {
            // 二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotBlank(courseSearchParam.getGrade())) {
            // 难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }
        // 分页-------------------------------------------------------
        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 20;
        }
        int start = (page - 1) * size;
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(size);
        // 设置bool查询到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);

        // 高亮设置------------------------------------------------------
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        // 设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);

        // 执行搜索
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest);
        } catch (IOException e) {
            ExceptionCast.cast(CommonCode.FAIL);
        }

        QueryResult<CoursePub> queryResult = new QueryResult<>();
        // 数据列表
        List<CoursePub> list = new ArrayList<>();

        // 获取响应结果
        SearchHits hits = searchResponse.getHits();
        // 匹配的总记录数
        long totalHits = hits.getTotalHits();
        queryResult.setTotal(totalHits);
        // 结果集处理
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            CoursePub coursePub = new CoursePub();
            // 取出source
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            // 取出名称
            String name = (String) sourceAsMap.get("name");
            // 取出高亮字段内容
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields != null) {
                HighlightField nameField = highlightFields.get("name");
                if (nameField != null) {
                    Text[] fragments = nameField.getFragments();
                    StringBuilder stringBuffer = new StringBuilder();
                    for (Text str : fragments) {
                        stringBuffer.append(str.string());
                    }
                    name = stringBuffer.toString();
                }
            }
            coursePub.setName(name);
            // 图片
            String pic = (String) sourceAsMap.get("pic");
            coursePub.setPic(pic);
            // 价格
            Float price = null;
            if (sourceAsMap.get("price") != null) {
                price = (Float) sourceAsMap.get("price");
            }
            coursePub.setPrice(price);
            Float price_old = null;
            if (sourceAsMap.get("price_old") != null) {
                price_old = (Float) sourceAsMap.get("price_old");
            }
            coursePub.setPrice_old(price_old);
            list.add(coursePub);
        }
        queryResult.setList(list);
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }
}
