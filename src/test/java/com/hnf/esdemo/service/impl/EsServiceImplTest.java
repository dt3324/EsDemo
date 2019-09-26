package com.hnf.esdemo.service.impl;

import com.hnf.esdemo.service.EsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class EsServiceImplTest {

    @Autowired
    private EsService esService;
    @Test
    public void search() {
        List<Map<String, Object>> search = esService.search("513432199009060816");
        for (Map<String, Object> map : search) {
            System.out.println(map);
        }

    }
}