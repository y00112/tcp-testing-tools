package com.wukong.service;

import com.wukong.common.Result;

/**
 * @author: 小山
 * @date: 2023/3/17
 * @content:
 */
public interface TcpService {

    Result connect();

    Result disconnect(Integer port);
}
