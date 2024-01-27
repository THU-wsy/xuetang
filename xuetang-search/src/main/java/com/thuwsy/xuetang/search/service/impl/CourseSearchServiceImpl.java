package com.thuwsy.xuetang.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.thuwsy.xuetang.base.model.PageParams;
import com.thuwsy.xuetang.search.dto.SearchCourseParamDto;
import com.thuwsy.xuetang.search.dto.SearchPageResultDto;
import com.thuwsy.xuetang.search.po.CourseIndex;
import com.thuwsy.xuetang.search.service.CourseSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: CourseSearchServiceImpl
 * Package: com.thuwsy.xuetang.search.service.impl
 * Description:
 *
 * @Author THU_wsy
 * @Create 2024/1/25 12:14
 * @Version 1.0
 */
@Slf4j
@Service
public class CourseSearchServiceImpl implements CourseSearchService {
    @Value("${elasticsearch.course.index}")
    private String courseIndexName;

    @Autowired
    private ElasticsearchClient client;

    @Override
    public SearchPageResultDto<CourseIndex> queryCoursePubIndex(PageParams pageParams, SearchCourseParamDto searchCourseParamDto) {
        // 1. 获取分页参数
        Long pageNo = pageParams.getPageNo();
        Long pageSize = pageParams.getPageSize();
        int from = (int) ((pageNo - 1) * pageSize);
        int size = pageSize.intValue();

        // 2. 检索的相关参数
        BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();
        // 匹配检索内容
        String keywords = searchCourseParamDto.getKeywords();
        if (StringUtils.isNotBlank(keywords)) {
            Query query1 = MultiMatchQuery.of(builder -> builder
                    .fields("name", "description")
                    .query(keywords))._toQuery();
            boolQueryBuilder.must(query1);
        }

        // 匹配大分类
        String mtName = searchCourseParamDto.getMt();
        if (StringUtils.isNotBlank(mtName)) {
            Query query2 = TermQuery.of(builder -> builder
                    .field("mtName")
                    .value(mtName))._toQuery();
            boolQueryBuilder.filter(query2);
        }

        // 匹配小分类
        String stName = searchCourseParamDto.getSt();
        if (StringUtils.isNotBlank(stName)) {
            Query query3 = TermQuery.of(builder -> builder
                    .field("stName")
                    .value(stName))._toQuery();
            boolQueryBuilder.filter(query3);
        }

        // 匹配难度
        String grade = searchCourseParamDto.getGrade();
        if (StringUtils.isNotBlank(grade)) {
            Query query4 = TermQuery.of(builder -> builder
                    .field("grade")
                    .value(grade))._toQuery();
            boolQueryBuilder.filter(query4);
        }

        // 3. 组装成bool查询
        BoolQuery boolQuery = boolQueryBuilder.build();

        // 4. 进行布尔查询
        SearchResponse<CourseIndex> response = null;
        try {
            response = client.search(builder -> builder
                            .index(courseIndexName)
                            .query(q -> q.bool(boolQuery))
                            .from(from)
                            .size(size)
                            .aggregations("mtAgg", a -> a   // 聚合名称
                                    .terms(t -> t.field("mtName").size(100)))
                            .aggregations("stAgg", a -> a   // 聚合名称
                                    .terms(t -> t.field("stName").size(100)))
                            .highlight(h -> h.fields("name", e -> e   // 高亮显示
                                    .preTags("<font class='eslight'>")
                                    .postTags("</font>")))
                    , CourseIndex.class);
        } catch (IOException e) {
            log.error("课程搜索异常：{}", e.getMessage());
            return new SearchPageResultDto<>(new ArrayList<>(), 0, 0, 0);
        }

        // 5. 返回结果处理
        // 获取查询到的文档总数
        long totalHits = getTotalHits(response);
        // 获取查询到的文档集合
        List<CourseIndex> list = getCourseList(response);
        // 获取聚合结果
        List<String> mtList = getAggregation(response, "mtAgg");
        List<String> stList = getAggregation(response, "stAgg");
        // 封装返回结果
        SearchPageResultDto<CourseIndex> pageResult = new SearchPageResultDto<>(list, totalHits, pageNo, pageSize);
        pageResult.setMtList(mtList);
        pageResult.setStList(stList);
        return pageResult;
    }

    /**
     * 获取查询到的文档总数
     */
    private long getTotalHits(SearchResponse<CourseIndex> response) {
        // 解析响应
        HitsMetadata<CourseIndex> metadata = response.hits();
        // 获取查询到的文档总数
        return metadata.total().value();
    }

    /**
     * 获取查询到的文档集合
     */
    private List<CourseIndex> getCourseList(SearchResponse<CourseIndex> response) {
        // 1. 解析响应
        HitsMetadata<CourseIndex> metadata = response.hits();

        // 2. 获取所有文档
        List<CourseIndex> list = new ArrayList<>();
        List<Hit<CourseIndex>> hits = metadata.hits();
        for (Hit<CourseIndex> ele : hits) {
            CourseIndex source = ele.source();

            // 3. 获取高亮信息
            Map<String, List<String>> highlightMap = ele.highlight();
            if (highlightMap != null && highlightMap.size() != 0) {
                String newName = highlightMap.get("name").get(0);
                source.setName(newName);
            }

            // 4. 添加对象到集合中
            list.add(source);
        }
        return list;
    }


    /**
     * 根据聚合名称，获取聚合结果
     */
    private List<String> getAggregation(SearchResponse<CourseIndex> response, String aggName) {
        StringTermsAggregate terms = response.aggregations().get(aggName).sterms();
        List<StringTermsBucket> buckets = terms.buckets().array();
        ArrayList<String> brandList = new ArrayList<>();
        for (StringTermsBucket bucket : buckets) {
            brandList.add(bucket.key()._toJsonString());
        }
        return brandList;
    }


}
