package com.zwb.demo.xc.test.search;

import com.zwb.demo.xc.search.SearchApplication;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/** Created by zwb on 2019/10/22 11:33 */
@SpringBootTest(classes = {SearchApplication.class})
@RunWith(SpringRunner.class)
public class TestSearch {

    @Resource RestHighLevelClient client;
    @Resource RestClient restClient;

    /** 搜索全部记录 */
    @Test
    public void test1() throws IOException, ParseException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 索索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** 分页查询 */
    @Test
    public void test2() throws ParseException, IOException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // -----------------------设置分页参数----------------------------------
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数
        // -----------------------设置分页参数----------------------------------
        // 索索全部
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** term query */
    @Test
    public void test3() throws ParseException, IOException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置分页参数
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数
        // 索索全部
        // ------------------ term query----------------------
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "spring"));
        // ------------------ term query----------------------
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        System.out.println(totalHits);
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** 根据id查询 */
    @Test
    public void test4() throws IOException, ParseException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数
        // 索索全部
        // -------------------------根据id查询------------------------------
        // 定义id
        String[] ids = new String[] {"1", "2"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));
        // -------------------------根据id查询------------------------------
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        System.out.println(totalHits);
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** match query */
    @Test
    public void test5() throws ParseException, IOException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数
        // 索索全部
        // ----------------- match query -------------------
        searchSourceBuilder.query(
                QueryBuilders.matchQuery("description", "spring开发框架").minimumShouldMatch("70%"));
        // ----------------- match query -------------------
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        System.out.println(totalHits);
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** multi_match query 可以设置多个词 */
    @Test
    public void test6() throws IOException, ParseException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数
        // 索索全部
        // ----------------- multi_match query -------------------
        searchSourceBuilder.query(
                QueryBuilders.multiMatchQuery("spring css", "description", "name")
                        .minimumShouldMatch("70%")
                        .field("name", 10));
        // ----------------- multi_match query -------------------
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        System.out.println(totalHits);
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** boolean query */
    @Test
    public void test7() throws IOException, ParseException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数
        // 索索全部
        // ----------------- boolean query -------------------
        // 定义一个multiMatchQueryBuilder
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("spring css", "description", "name")
                        .minimumShouldMatch("70%")
                        .field("name", 10);
        // 定义一个termQueryBuilder
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "21001");
        // 定义一个boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        // ----------------- boolean query -------------------
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        System.out.println(totalHits);
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** 过滤器 */
    @Test
    public void test8() throws IOException, ParseException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数
        // 索索全部
        // ----------------- 过虑 -------------------
        // 定义一个multiMatchQueryBuilder
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("spring css", "description", "name")
                        .minimumShouldMatch("70%")
                        .field("name", 10);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        // 定义一个boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(searchSourceBuilder.query());

        // 过虑
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        // ----------------- 过虑 -------------------
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        System.out.println(totalHits);
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(name);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
        }
    }

    /** 高亮 */
    @Test
    public void test9() throws IOException, ParseException {
        // 请求搜索对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 设置类型
        searchRequest.types("doc");
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        int page = 1;
        int size = 1;
        int from = (page - 1) * size;
        searchSourceBuilder.from(from); // 起始下标从0开始
        searchSourceBuilder.size(size); // 每页显示记录数

        // 索索全部
        // 定义一个multiMatchQueryBuilder
        MultiMatchQueryBuilder multiMatchQueryBuilder =
                QueryBuilders.multiMatchQuery("开发框架", "description", "name")
                        .minimumShouldMatch("70%")
                        .field("name", 10);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        // 定义一个boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(searchSourceBuilder.query());

        // 过虑
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        // ----------------高亮-------------
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.field("name");
        searchSourceBuilder.highlighter(highlightBuilder);
        // ----------------高亮-------------
        // source源字段过滤
        searchSourceBuilder.fetchSource(
                new String[] {"name", "studymodel", "price", "timestamp"}, new String[] {});
        // 设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索匹配结果
        SearchHits hits = searchResponse.getHits();
        // 搜索总记录数
        long totalHits = hits.totalHits;
        System.out.println(totalHits);
        // 匹配度较高的文档
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String id = searchHit.getId();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 拿不到，因为设置了过滤
            String description = (String) sourceAsMap.get("description");
            double price = (double) sourceAsMap.get("price");
            String timestamp = (String) sourceAsMap.get("timestamp");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = dateFormat.parse(timestamp);
            System.out.println(id);
            System.out.println(description);
            System.out.println(price);
            System.out.println(parse);
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            if (highlightFields != null) {
                HighlightField nameField = highlightFields.get("name");
                if (nameField != null) {
                    Text[] fragments = nameField.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text text : fragments) {
                        stringBuffer.append(text.toString());
                    }
                    name = stringBuffer.toString();
                }
            }
            System.out.println(name);
        }
    }
}
