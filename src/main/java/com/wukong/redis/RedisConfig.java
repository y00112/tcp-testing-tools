package com.wukong.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author: 小山
 * @date: 2023/4/9
 * @content:
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory
           /* , CustomerKeyExpirationListener customerKeyExpirationListener */) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 自定义key 过期监听
//        container.addMessageListener(customerKeyExpirationListener,new PatternTopic("__keyevent@*__:expired"));
        return container;
    }

}
