package com.hnf.esdemo.service;

import java.util.List;
import java.util.Map;

/**
 * @author dt
 */
public interface EsService {
    List<Map<String, Object>> search(String search);
    String searchAll(String index,String type,String search);
}
