package com.wukong.session;

import io.netty.channel.ChannelId;

import java.util.ArrayList;

/**
 * @author: 小山
 * @date: 2023/4/8
 * @content: 会话管理器
 */
public interface Session {

    /**
     * 绑定Tcp服务器
     * @param
     * @param port
     */
    void bind(Thread tcpServerThread,int port);

    void close(int port);

    void bindClientId(Integer port, ChannelId clientId);

    void removeClientId(Integer port,ChannelId clientId);

    ArrayList<ChannelId> getClientIds(Integer port);
}
