package com.hnf.esdemo.util;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.util.List;

/**
 * @author admin
 */
public class EsUtil {
    /**
     * 拼接 查询条件
     * @param search 查询条件
     * @param filedNames 查询的字段
     * @return queryBuilders
     */
    public static QueryBuilder getQueryBuilders(String search, List<String> filedNames) {
        String[] strs = search.split(" ");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String str : strs) {
            if (str != null && !str.trim().isEmpty()) {
                for (String field : filedNames) {
                    boolQueryBuilder.should(QueryBuilders.matchQuery(field,str));
                }
            }
        }
        return  QueryBuilders.boolQuery().must(boolQueryBuilder);
    }

    /**
     * 高亮字段
     * @param fields 高亮字段
     * @return 高亮
     */
    public static HighlightBuilder getHighBuilder(List<String> fields) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        fields.forEach(highlightBuilder::field);
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red;font-size:14px\" class=\"hight-light\">");
        highlightBuilder.postTags("</span>");
        return highlightBuilder;
    }

    /**
     * 通过高亮对象获取其的字符串值
     *
     * @param highlightField 高亮对象
     * @return 返回高亮后的字符串
     */
    public static String getHighLightStr(HighlightField highlightField){
        Text[] fragments = highlightField.fragments();
        StringBuilder stringBuilder = new StringBuilder();
        for (Text fragment : fragments) {
            stringBuilder.append(fragment);
        }
        return stringBuilder.toString();
    }
}
