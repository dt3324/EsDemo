package com.hnf.esdemo.pool;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author dt
 */
@Component
@ConfigurationProperties(prefix = "cluster")
@Lazy(value = false)
public class EsPool {
    private static final int MIN = 2;
    private String ip;
    private String name;
    private Integer port;
    private Integer max;
    private ConcurrentLinkedDeque<Client> clients;
    private Settings setting;

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    /**
     * 创建连接池队列
     *
     * @param max  连接池初始容量
     * @param name name
     * @param ip   ip
     * @param port 端口
     * @return 连接池队列
     */
    private ConcurrentLinkedDeque<Client> createClient(Integer max, String name, String ip, Integer port) {
        ConcurrentLinkedDeque<Client> clients = new ConcurrentLinkedDeque<>();
        //避免netty版本冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        setting = Settings.builder().put("cluster.name", name).build();
        for (int i = 0; i < max; i++) {
            try {
                //创建Client添加到队列中
                clients.add(new PreBuiltTransportClient(setting).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port)));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return clients;
    }

    /**
     * 获取Clients中剩余的Client
     *
     * @return 返回数量
     */
    private synchronized int getSizeOfClients() {
        if (clients != null) {
            return clients.size();
        }
        return 0;
    }

    /**
     * 获取Client
     *
     * @return 返回Client
     */
    public synchronized Client getClient() {
        if (getSizeOfClients() == 0) {
            clients = createClient(max, name, ip, port);
        }
        //如果队列中的剩余Client少于最小数量就把再创建最大数量的Client装进队列中
        if (getSizeOfClients() <= MIN) {
            //有大量Client没有归还，实际容量接近之前容量的两倍
            for (int i = 0; i < max; i++) {
                try {
                    clients.add(new PreBuiltTransportClient(setting).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port)));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        //如果队列中数量大于最大数量的两倍就进行缩容
        if (getSizeOfClients() > max << 1) {
            clients = createClient(max, name, ip, port);
        }
        return clients.poll();
    }

    /**
     * 归还Client
     *
     * @param client 归还的连接
     */
    public synchronized void release(Client client) {
        clients.add(client);
    }
}
