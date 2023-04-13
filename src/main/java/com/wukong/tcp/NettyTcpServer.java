package com.wukong.tcp;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


/**
 * @author: 小山
 * @date: 2023/3/16
 * @content: Netty服务器
 */
@Slf4j
public class NettyTcpServer {
    private int port;
    private Channel channel;

    public NettyTcpServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        NettyServerHandler NETTY_HANDLER = new NettyServerHandler();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch) throws Exception {
                            // 添加Netty服务器的处理程序
                            ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                            ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
                            // 心跳
                            ch.pipeline().addLast(new IdleStateHandler(3, 0, 0, TimeUnit.MINUTES));
                            // 添加自定义的空闲状态处理器
                            ch.pipeline().addLast(new MyIdleStateHandler());
                            ch.pipeline().addLast(NETTY_HANDLER);
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            channel = f.channel();
            log.info("TCP server started on port " + port);
            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static class MyIdleStateHandler extends ChannelDuplexHandler {
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
                if (idleStateEvent.state() == IdleState.READER_IDLE) {
                    // 读空闲超时，强制关闭连接
                    ctx.close();
                }
            }
        }
    }
}