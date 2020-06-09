package com.hnf.esdemo.dao.impl;

import com.hnf.esdemo.pool.EsPool;
import com.hnf.esdemo.dao.EsApplication;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * es查询
 * @author dt
 */
@Component
public class EsApplicationImpl implements EsApplication {

    @Resource
    private EsPool esPool;

    /**
     * 根据条件查询es
     * @param index 相当于mysql的库名
     * @param type 相当于MySQL的表明
     * @param queryBuilder 查询条件
     * @param rqb 时间条件
     * @param hb 高亮条件
     * @param from 从那条数据开始
     * @param size 查询多少条数据
     * @return 返回查询结果
     */
    @Override
    public SearchHits searchByBuilders(String index,String type,QueryBuilder queryBuilder,RangeQueryBuilder rqb,
                                       HighlightBuilder hb,Integer from,Integer size){
        Client client = null;
        SearchResponse response;
        try{
            client = esPool.getClient();
            response = client.prepareSearch(index)
                    .setTypes(type)
                    .setQuery(queryBuilder)
                    .highlighter(hb)
                    .setFrom(from)
                    //设置查询方式 （效率最高）
                    .setSearchType(SearchType.QUERY_THEN_FETCH)
                    .setSize(size)
                    .execute()
                    .actionGet();
            if(response!=null){
                return response.getHits();
            }
        }finally {
            if(client!=null){
                esPool.release(client);
            }
        }
        return null;
    }

    /**
     * 修改es中的数据
     * @param index index
     * @param type type
     * @param id 唯一标识
     * @param document 更新的数据
     */
    @Override
    public void elasticUpdate(String index, String type, String id, Map document){
        Client client=null;
        try {
            client = esPool.getClient();
            client.prepareUpdate(index,type,id).setDoc(document).get();
        }finally {
            if(client!=null){
                esPool.release(client);
            }
        }
    }

    /**
     * 向es中添加数据
     * @param index
     * @param type
     * @param map
     */
    @Override
    public void elasticAdd(String index,String type,Map map){
        IndexRequest indexRequest = new IndexRequest()
                .index(index)
                .type(type)
                .source(map);
        Client client = null;
        try {
            client = esPool.getClient();
            client.index(indexRequest);
        }finally {
            if(client!=null){
                esPool.release(client);
            }
        }
    }
}
