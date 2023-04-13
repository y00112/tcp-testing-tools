package com.wukong.redis;

import com.wukong.session.SessionFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
/**
 * @author: 小山
 * @date: 2023/4/9
 * @content: 用于处理过期事件通知
 */

/**
 * Created by Administrator on 2020/12/10.
 */
@Component
public class KeyExpirationListener extends KeyExpirationEventMessageListener {

    public KeyExpirationListener(RedisMessageListenerContainer listenerContainer){
        super(listenerContainer);
    }


    public void onMessage(Message message, @Nullable byte[] pattern) {
        String key = message.toString();
        System.out.println("监听到key:" + key + "过期");
        SessionFactory.getSession().close(Integer.valueOf(key));
    }

}
