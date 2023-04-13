//package com.wukong.redis;
//
//import com.wukong.session.SessionFactory;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Component;
//
///**
// * @author: 小山
// * @date: 2023/4/9
// * @content: 自定义key过期监听
// */
//@Component
//public class CustomerKeyExpirationListener implements MessageListener {
//
//    @Override
//    public void onMessage(Message message, @Nullable byte[] pattern) {
//        String key = message.toString();
//        System.out.println("监听到key: " + key + " 过期");
//        SessionFactory.getSession().close(Integer.valueOf(key));
//    }
//
//}
//
