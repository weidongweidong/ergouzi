package com.ergouzi.eurekaproducer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: chenweidong
 * @create: 2021-08-12 20:11
 **/
@RestController
public class helloController {
    @RequestMapping("sayHello")
    public String sayHello(String param){
        return  "Hello"+ param;
    }

}
