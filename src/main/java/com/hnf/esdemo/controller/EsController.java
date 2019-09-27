package com.hnf.esdemo.controller;

import com.hnf.esdemo.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dt
 */
@RestController
@RequestMapping("/search")
public class EsController {
    @Autowired
    private EsService esService;
    @RequestMapping("/findAll")
    public List<String> findAll(){
        ArrayList<String> sb = new ArrayList<>();
        sb.add(esService.searchAll("infodata2", "t_case", null));
        sb.add(esService.searchAll("infodata2", "t_contact_phonenum", null));
        sb.add(esService.searchAll("infodata2", "t_device", null));
        sb.add(esService.searchAll("infodata2", "t_person", null));
        sb.add(esService.searchAll("infodata2", "t_qq_troop", null));
        sb.add(esService.searchAll("infodata2", "t_qquser", null));
        sb.add(esService.searchAll("infodata2", "t_wxchatroom", null));
        sb.add(esService.searchAll("infodata2", "t_wxuser", null));
        sb.add(esService.searchAll("infodata", "message", null));
        sb.add(esService.searchAll("infodata", "qqmsg", null));
        sb.add(esService.searchAll("infodata", "qqTroopMsg", null));
        sb.add(esService.searchAll("infodata", "record", null));
        sb.add(esService.searchAll("infodata", "wxChatroomMsg", null));
        sb.add(esService.searchAll("infodata", "wxmsg", null));
        return sb;
    }
}
