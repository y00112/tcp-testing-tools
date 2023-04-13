package com.wukong.session;

import io.netty.channel.ChannelId;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 小山
 * @date: 2023/3/16
 * @content:
 */
public class SessionMemoryImpl implements Session {


    private static final HashMap<Integer, Thread> TCP_THREAD = new HashMap<>();

    private static ConcurrentHashMap<Integer, ArrayList<ChannelId>> CLIENT_MAP = new ConcurrentHashMap<>();

    @Override
    public void bind(Thread tcpServerThread, int port) {
        TCP_THREAD.put(port, tcpServerThread);
    }

    @Override
    public void close(int port) {
        Thread thread = TCP_THREAD.get(port);
        thread.stop();
        TCP_THREAD.remove(port);
    }

    @Override
    public void removeClientId(Integer port, ChannelId clientId) {
        CLIENT_MAP.get(port).remove(clientId);
    }

    @Override
    public ArrayList<ChannelId> getClientIds(Integer port) {
        return CLIENT_MAP.get(port);
    }

    @Override
    public void bindClientId(Integer port, ChannelId clientId) {
        if (CLIENT_MAP.get(port) == null) {
            ArrayList<ChannelId> clientIdList = new ArrayList<>();
            clientIdList.add(clientId);
            CLIENT_MAP.put(port, clientIdList);
        } else {
            CLIENT_MAP.get(port).add(clientId);
        }
    }

}
