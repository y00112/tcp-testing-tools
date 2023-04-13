package com.wukong.service.impl;

import com.wukong.common.Result;
import com.wukong.service.AsyncService;
import com.wukong.service.TcpService;
import com.wukong.session.SessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author: 小山
 * @date: 2023/3/17
 * @content:
 */
@Service
@Slf4j
public class TcpServiceImpl implements TcpService {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private RedisTemplate stringRedisTemplate;

    @Override
    public Result connect() {
        int port = getRandomPort();

        asyncService.startTcpServer(port);
        stringRedisTemplate.opsForValue().set(String.valueOf(port), "thread", 60 * 15, TimeUnit.SECONDS);

        return Result.success(port);
    }

    @Override
    public Result disconnect(Integer port) {
        try {
            SessionFactory.getSession().close(port);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("500");
        }
        log.info("Tcp Server Close on Port {}", port);
        return Result.success();
    }


    private int getRandomPort() {
        // 生成随机端口号的代码
        Integer port = (new Random().nextInt(65535) + 10000);
        return port;
    }
}
