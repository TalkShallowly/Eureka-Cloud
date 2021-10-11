package com.talkshallowly.eurekaprovider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Value("${server.port}")
    String port;

    @GetMapping("/getMsg")
    public String getMsg(){
        return "端口： " + port;
    }
}
