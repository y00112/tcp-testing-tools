package com.wukong.service.impl;

import com.wukong.service.AsyncService;
import com.wukong.session.SessionFactory;
import com.wukong.tcp.NettyTcpServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: 小山
 * @date: 2023/4/8
 * @content: 异步方法
 */
@Service
public class AsyncServiceImpl implements AsyncService {


    @Async
    @Override
    public void startTcpServer(int port) {
        Thread threadTcpServer = new ThreadTcpServer(port);
        SessionFactory.getSession().bind(threadTcpServer,port);
        threadTcpServer.start();
    }


    class ThreadTcpServer extends Thread{

        int port;

        public ThreadTcpServer(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            NettyTcpServer tcpServer = new NettyTcpServer(port);
            try {
                tcpServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
