package com.wukong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class TcpLabApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TcpLabApplication.class, args);
    }
}
