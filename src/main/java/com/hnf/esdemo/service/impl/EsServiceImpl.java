package com.hnf.esdemo.service.impl;

import com.hnf.esdemo.dao.EsApplication;
import com.hnf.esdemo.service.EsService;
import com.hnf.esdemo.util.EsUtil;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author dt
 */
@Service
public class EsServiceImpl implements EsService {
    @Autowired
    private EsApplication esApplication;

    @Override
    public List<Map<String, Object>> search(String search) {
        QueryBuilder queryBuilder = EsUtil.getQueryBuilders(search, Arrays.asList("casename", "usernumber"));
        HighlightBuilder highlightBuilder = EsUtil.getHighBuilder(Arrays.asList("casename", "usernumber"));
        SearchHits searchHits = esApplication.searchByBuilders("infodata2", "t_case", queryBuilder,
                null, highlightBuilder, 0, 1);
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> source = searchHit.getSource();
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField personName = highlightFields.get("casename");
            if (personName != null) {
                source.put("casename",EsUtil.getHighLightStr(personName));
            }
            HighlightField userNumber = highlightFields.get("usernumber");
            if (userNumber != null) {
                source.put("usernumber",EsUtil.getHighLightStr(userNumber));
            }
            results.add(source);
        }
        return results;
    }

    @Override
    public String searchAll(String index, String type, String search) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("查询");
        stringBuilder.append(index);
        stringBuilder.append("库的");
        stringBuilder.append(type);
        stringBuilder.append("表，");
        SearchHits searchHits = esApplication.searchByBuilders(index, type, null, null, null, 0, 100);
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : searchHits.getHits()) {
            Map<String, Object> source = hit.getSource();
            result.add(source);
        }
        if(result.isEmpty()){
            stringBuilder.append("查询结果为空，");
             return stringBuilder.toString();
        }
        stringBuilder.append("查询出 “  ").append(result.size()).append("  ” 条数据！");
        return stringBuilder.toString();
    }
}
