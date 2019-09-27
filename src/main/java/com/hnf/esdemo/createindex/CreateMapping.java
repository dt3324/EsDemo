/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hnf.esdemo.createindex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import com.hnf.esdemo.pool.EsPool;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用于创建对应的mapping文件
 *
 * @author yy
 */
@Component
public class CreateMapping {
    private static final Logger logger = LoggerFactory.getLogger(CreateMapping.class);

    @Autowired
    private EsPool esPool;


    /**
     * 用于新建索引时创建其的mapping文件
     *
     */
    public void start() {
        try {
            /**
             * es的连接配置信息
             */
            Client client = esPool.getClient();
            if (client.admin().indices().prepareExists("infodata2").get().isExists()) {
                client.admin().indices().prepareDelete("infodata2").get();
            }
            if (client.admin().indices().prepareExists("infodata").get().isExists()) {
                client.admin().indices().prepareDelete("infodata").get();
            }
            String caseStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_case.json");
            String contactStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_contact_phonenum.json");
            String deviceStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_device.json");
            String personStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_person.json");
            String qqTroopStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_qq_troop.json");
            String qqUserStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_qquser.json");
            String wxChatRoomStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_wxchatroom.json");
            String wxUserStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\t_wxuser.json");

            client.admin().indices().prepareCreate("infodata2")
                    //wx群消息的mapping
                    .addMapping("t_case", caseStr)
                    //短消息的mapping
                    .addMapping("t_contact_phonenum", contactStr)
                    //wx朋友聊天激励的mapping
                    .addMapping("t_device", deviceStr)
                    .addMapping("t_person", personStr)
                    .addMapping("t_qq_troop", qqTroopStr)
                    .addMapping("t_qquser", qqUserStr)
                    .addMapping("t_wxchatroom", wxChatRoomStr)
                    .addMapping("t_wxuser", wxUserStr)
                    .get();
            if (client.admin().indices().prepareExists("infodata").get().isExists()) {
                client.admin().indices().prepareDelete("infodata").get();
            }
            String mappingStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\wxChatroomMsg.json");
            String messageStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\message.json");
            String wxmsg = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\wxmsg.json");
            String qqTroopMsgStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\qqTroopMsg.json");
            String qqmsgStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\qqmsg.json");
            String recordStr = buildMapping("E:\\test\\demo\\EsDemo\\src\\main\\resources\\config\\record.json");
            client.admin().indices().prepareCreate("infodata")
                    //wx群消息的mapping
                    .addMapping("wxChatroomMsg", mappingStr)
                    //短消息的mapping
                    .addMapping("message", messageStr)
                    //wx朋友聊天激励的mapping
                    .addMapping("wxmsg", wxmsg)
                    .addMapping("qqTroopMsg", qqTroopMsgStr)
                    .addMapping("qqmsg", qqmsgStr)
                    .addMapping("record", recordStr)
                    .get();
//            //此处用于创建对应的索引
//            TestUnique tu = new TestUnique();
//            //创建通话记录的索引
//            tu.createRecordMapping("", "");
//            //创建qq好友聊天信息的索引
//            tu.creatQQMsgMapping();
//            //创建wx好友聊天信息的索引
//            tu.creatWxMsgMapping();
//            //用户创建短消息聊天信息的索引
//            tu.createMessageMapping("", "");
//            //用于创建qq群聊天信息的索引
//            tu.createQQTroopMsgMapping("", "");
//            //用户创建wx群聊天信息的索引
//            tu.createMapping("", "");
            System.out.println("索引创建成功！！");
            logger.info("索引创建成功！！");
        } catch (Exception e) {
            logger.error("索引创建失败");
            e.printStackTrace();
        }

    }

    /**
     * 用于创建索引的mapping文件
     *
     * @param path 对应文件的路径
     * @return 返回对应的json字符串
     * @throws FileNotFoundException 判处李静用错误的文件
     */
    private static String buildMapping(String path) throws FileNotFoundException {
        InputStream in = new FileInputStream(new File(path));
        System.out.println(in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder builder = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
