package com.ergouzi.eurekaconsumer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 消费示例controller
 * @author: chenweidong
 * @create: 2021-07-05 20:22
 **/
@RestController
public class DemoController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("hello")
    public String sayHello(@RequestParam String name){
        return restTemplate.getForObject("http://EUREKA-PRODUCER/sayHello?param=" + name, String.class);
    }

    @RequestMapping("douyin")
    public JSONObject resolving(String url){
        String forObject = restTemplate.getForObject("http://EUREKA-PRODUCER/dyresolving?url=" + url, String.class);
        JSONObject jsonObject = JSON.parseObject(forObject);
        return jsonObject;
    }

}
