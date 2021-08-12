package com.ergouzi.eurekaproducer.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @description:
 * @author: chenweidong
 * @create: 2021-08-12 20:13
 **/
public class ResultUtil {

    public static JSONObject success(String msg,Object data){
        JSONObject jsonObject = new JSONObject(){{
            put("status", 0);
            put("msg",msg);
            put("data",data);
        }};
        return  jsonObject;
    }


    public static  JSONObject error(String msg){
        JSONObject jsonObject = new JSONObject(){{
            put("status", 1);
            put("msg",msg);
            put("data",null);
        }};
        return  jsonObject;
    }
}
