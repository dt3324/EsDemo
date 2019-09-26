package com.hnf.esdemo.service.impl;

import com.hnf.esdemo.service.EsApplication;
import com.hnf.esdemo.service.EsService;
import com.hnf.esdemo.util.EsUtil;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author dt
 */
@Service
public class EsServiceImpl implements EsService {
    @Autowired
    private EsApplication esApplication;

    @Override
    public List<Map<String, Object>> search(String search) {
        QueryBuilder queryBuilder = EsUtil.getQueryBuilders(search, Arrays.asList("personname", "usernumber"));
        HighlightBuilder highlightBuilder = EsUtil.getHightBuilder(Arrays.asList("personname", "usernumber"));
        SearchHits searchHits = esApplication.searchByBuilders("infodata2", "t_person", queryBuilder, null, highlightBuilder, 0, 1);
        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> source = searchHit.getSource();
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField personName = highlightFields.get("personname");
            if (personName != null) {
                source.put("personname",EsUtil.getHightLightStr(personName));
            }
            HighlightField userNumber = highlightFields.get("usernumber");
            if (userNumber != null) {
                source.put("usernumber",EsUtil.getHightLightStr(userNumber));
            }
            results.add(source);
        }
        return results;
    }
}
