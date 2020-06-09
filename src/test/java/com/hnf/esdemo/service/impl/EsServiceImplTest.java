package com.hnf.esdemo.service.impl;

import com.hnf.esdemo.createindex.CreateMapping;
import com.hnf.esdemo.dao.EsApplication;
import com.hnf.esdemo.service.EsService;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EsServiceImplTest {

    @Autowired
    private CreateMapping createMapping;

    @Autowired
    private EsApplication esApplication;
    @Autowired
    private EsService esService;
    @Test
    public void search() {
        List<Map<String, Object>> search = esService.search("410927198809264011");
        for (Map<String, Object> map : search) {
            System.out.println(map);
        }
    }
    @Test
    public void searchAdd() {
        Map<String,String> map = new HashMap();
        map.put("caseuniquemark","CB30E551E740C6D79F418748B898CB67");
        map.put("casename","2019.05.07孟庆省无证驾驶");
        map.put("casenumb","A513432070302019050014");
        map.put("casetype","其他");
        map.put("usernumber","410927198809264011");
        esApplication.elasticAdd("infodata2","t_case",map);
    }
    @Test
    public void start() {
        createMapping.start();
    }
@Test
    public void findAll() {
    Map<String, Object> searchAll = esService.searchAll("infodata2", "t_person", null);
    System.out.println(searchAll);
}



}