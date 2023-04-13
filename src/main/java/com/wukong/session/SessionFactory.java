package com.wukong.session;

/**
 * @author: 小山
 * @date: 2023/4/8
 * @content:
 */
public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession(){
        return session;
    }
}
