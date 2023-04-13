package com.wukong.controller;

import com.wukong.common.Result;
import com.wukong.service.TcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author: 小山
 * @date: 2023/3/17
 * @content:
 */
@RestController
@RequestMapping("/api")
public class TcpController {

    @Autowired
    private TcpService tcpService;

    @GetMapping("/connect")
    @CrossOrigin
    public Result createConnect(){
        return tcpService.connect();
    }

    @PostMapping("/disconnect")
    @CrossOrigin
    public Result closeConnect(@RequestParam Integer port){
        return tcpService.disconnect(port);
    }
}
