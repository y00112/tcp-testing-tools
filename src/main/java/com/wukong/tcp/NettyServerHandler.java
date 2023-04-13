package com.wukong.tcp;

import com.wukong.session.SessionFactory;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.wukong.websocket.WebSocketServer.BroadCastInfo;
import static com.wukong.websocket.WebSocketServer.SendMessage;

/**
 * @author: 小山
 * @date: 2023/3/16
 * @content: netty处理器
 */

@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static Lock lock_1 = new ReentrantLock();

    private static Lock lock_2 = new ReentrantLock();

    private static Lock lock_3 = new ReentrantLock();

    private static Lock lock_4 = new ReentrantLock();


    //管理一个全局map，保存连接进服务端的通道数量

    public static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();


    public NettyServerHandler() {
    }


    /**
     * 处理异常, 一般是需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        log.info("服务端异常关闭" + ctx.channel());
    }


    /**
     * @param ctx
     * @DESCRIPTION: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        lock_1.lock();
        try {
            //获取连接通道唯一标识
            ChannelId channelId = ctx.channel().id();
            //如果map中不包含此连接，就保存连服务端异常关闭接
            if (CHANNEL_MAP.containsKey(channelId)) {
                log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size());
            } else {
                //保存连接
                CHANNEL_MAP.put(channelId, ctx);
                // 获取服务器端口号
                InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().localAddress();
                int port = socketAddress.getPort();
                SessionFactory.getSession().bindClientId(port, channelId);

                SendMessage("客户端 "+ctx.channel()+" 连接服务器",port);
                log.info("客户端【" + channelId + "】连接netty服务器");
                log.info("连接通道数量: " + CHANNEL_MAP.size());
            }
        } catch (Exception e) {
            log.error("客户端连接错误:" + e.getMessage());
        } finally {
            lock_1.unlock();
        }
    }

    /**
     * @param ctx
     * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        lock_2.lock();
        try {
            ChannelId channelId = ctx.channel().id();
            // 获取服务器端口号
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().localAddress();
            int port = socketAddress.getPort();
            SessionFactory.getSession().removeClientId(port, channelId);
            //包含此客户端才去删除
            if (CHANNEL_MAP.containsKey(channelId)) {
                //删除连接
                CHANNEL_MAP.remove(channelId);
                System.out.println();
                SendMessage("客户端 "+ctx.channel()+" 退出服务器",port);
                log.info("客户端【" + channelId + "】退出netty服务器");
                log.info("连接通道数量: " + CHANNEL_MAP.size());
            }
        } catch (Exception e) {
            log.error("客户端断开连接错误:" + e.getMessage());
        } finally {
            lock_2.unlock();
        }
    }


    /**
     * 1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
     * 2. Object msg: 就是客户端发送的数据 默认Object
     * <p>
     * 读取数据实际(这里我们可以读取客户端发送的消息)
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        lock_3.lock();
        try {
            log.info("服务器读取线程 " + Thread.currentThread().getName() + " channle = " + ctx.channel());
            log.info("接收消息：{}", msg);
            Channel channel = ctx.channel();
            // 发送给指定端口号的 WebSocket
            InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().localAddress();
            int port = socketAddress.getPort();
            SendMessage("客户端【" + channel.id() + "】,发送：" + msg, port);
            // 发送给客户端
            CHANNEL_MAP.get(ctx.channel().id()).writeAndFlush(msg);

        } catch (Exception e) {
            log.error("读取数据失败:" + e.getMessage());
        } finally {
            lock_3.unlock();
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        lock_4.lock();
        try {
            String socketString = ctx.channel().remoteAddress().toString();
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.READER_IDLE) {
                    log.info("Client: " + socketString + " READER_IDLE 读超时");
                    ctx.disconnect();
                } else if (event.state() == IdleState.WRITER_IDLE) {
                    log.info("Client: " + socketString + " WRITER_IDLE 写超时");
                    ctx.disconnect();
                } else if (event.state() == IdleState.ALL_IDLE) {
                    log.info("Client: " + socketString + " ALL_IDLE 总超时");
                    ctx.disconnect();
                }
            }
        } catch (Exception e) {
            log.error("error:" + e.getMessage());
        } finally {
            lock_4.unlock();
        }
    }

}
