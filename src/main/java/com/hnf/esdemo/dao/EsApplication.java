package com.hnf.esdemo.dao;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.util.Map;

public interface EsApplication {
    SearchHits searchByBuilders(String index,
                                String type,
                                QueryBuilder queryBuilder,
                                RangeQueryBuilder rqb,
                                HighlightBuilder hb,
                                Integer from,
                                Integer size);

    void elasticUpdate(String index, String type, String id, Map document);

    void elasticAdd(String index,String type,Map map);
}
