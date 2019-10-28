package com.zwb.demo.xc.search.service;

import com.zwb.demo.xc.common.exception.ExceptionCast;
import com.zwb.demo.xc.common.model.response.CommonCode;
import com.zwb.demo.xc.common.model.response.QueryResponseResult;
import com.zwb.demo.xc.common.model.response.QueryResult;
import com.zwb.demo.xc.domain.course.CoursePub;
import com.zwb.demo.xc.domain.course.TeachplanMediaPub;
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
import java.util.HashMap;
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

    @Value("${xuecheng.elasticsearch.media.index}")
    private String media_index;

    @Value("${xuecheng.elasticsearch.media.type}")
    private String media_type;

    @Value("${xuecheng.elasticsearch.media.source_field}")
    private String media_source_field;

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
                price = Float.parseFloat(String.valueOf(sourceAsMap.get("price")));
            }
            coursePub.setPrice(price);
            Float price_old = null;
            if (sourceAsMap.get("price_old") != null) {
                price_old = Float.parseFloat(String.valueOf(sourceAsMap.get("price_old")));
            }
            String id = (String) sourceAsMap.get("id");
            coursePub.setId(id);
            coursePub.setPrice_old(price_old);
            list.add(coursePub);
        }
        queryResult.setList(list);
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }

    public Map<String, CoursePub> getall(String id) {
        // 定义一个搜索对象
        SearchRequest searchRequest = new SearchRequest(es_index);
        // type
        searchRequest.types(es_type);
        // searchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("id", id));
        // 过滤字段

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String, CoursePub> map = new HashMap<>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String courseId = (String) sourceAsMap.get("id");
            String name = (String) sourceAsMap.get("name");
            String grade = (String) sourceAsMap.get("grade");
            String charge = (String) sourceAsMap.get("charge");
            String pic = (String) sourceAsMap.get("pic");
            String description = (String) sourceAsMap.get("description");
            String teachplan = (String) sourceAsMap.get("teachplan");
            CoursePub coursePub = new CoursePub();
            coursePub.setId(courseId);
            coursePub.setName(name);
            coursePub.setPic(pic);
            coursePub.setGrade(grade);
            coursePub.setTeachplan(teachplan);
            coursePub.setDescription(description);
            map.put(courseId, coursePub);
        }
        return map;
    }

    /**
     * 根据多个id搜索课程计划信息
     *
     * @return
     */
    public QueryResponseResult<TeachplanMediaPub> getmedia(String[] teachplanIds) {
        // 定义一个搜索对象
        SearchRequest searchRequest = new SearchRequest(media_index);
        // type
        searchRequest.types(media_type);
        // searchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 过滤字段
        String[] split = media_source_field.split(",");
        searchSourceBuilder.fetchSource(split, new String[] {});
        // 使用termQuery用多个id查询
        // -----------termsQuery----------------------????????????---------
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id", teachplanIds));

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = client.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        Map<String, CoursePub> map = new HashMap<>(); // 数据列表
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取出课程计划媒资信息
            String courseid = (String) sourceAsMap.get("courseid");
            String media_id = (String) sourceAsMap.get("media_id");
            String media_url = (String) sourceAsMap.get("media_url");
            String teachplan_id = (String) sourceAsMap.get("teachplan_id");
            String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");
            teachplanMediaPub.setCourseId(courseid);
            teachplanMediaPub.setMediaUrl(media_url);
            teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
            teachplanMediaPub.setMediaId(media_id);
            teachplanMediaPub.setTeachplanId(teachplan_id); // 将数据加入列表
            teachplanMediaPubList.add(teachplanMediaPub);
        }
        // 构建返回课程媒资信息对象
        QueryResult<TeachplanMediaPub> queryResult = new QueryResult<>();
        queryResult.setList(teachplanMediaPubList);
        queryResult.setTotal(hits.getTotalHits());
        QueryResponseResult<TeachplanMediaPub> queryResponseResult =
                new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}
