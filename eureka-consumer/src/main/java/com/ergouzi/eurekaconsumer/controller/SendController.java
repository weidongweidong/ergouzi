package com.ergouzi.eurekaconsumer.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Component
@EnableScheduling
public class SendController {
    @Value("${wx.appID}")
    private String appID;
    @Value("${wx.appsecret}")
    private String appsecret;

    @Scheduled(cron = "0 30 7 * * ? ")
    public void push(){
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appID);
        wxStorage.setSecret(appsecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("oyB1H6phb2WXgXjHux5NbgHu70T0")
                .templateId("KZFuzw4_KdNdmcdi8JlLvxFThl6ufFENfwoBgEMAwL8")
                .build();
        //3,发送模版消息，这里需要配置你的信息

//        String url = "https://geoapi.qweather.com/v2/city/lookup?location=101010700&key=95cf38cd40b84df9beae340c610e8550";
        String url = "http://t.weather.itboy.net/api/weather/city/101010700";
        String s1 = HttpUtil.get(url);
        JSONObject tianqi = JSONObject.parseObject(s1);
        if(tianqi != null && tianqi.getInteger("status") == 200 ){
            JSONObject data = tianqi.getJSONObject("data");
            JSONObject cityInfo = tianqi.getJSONObject("cityInfo");
            JSONArray forecast = data.getJSONArray("forecast");
            templateMessage.addData(new WxMpTemplateData("time",format(),"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("city",cityInfo.getString("parent") + " " + cityInfo.getString("city") ,"#4169E1"));
            templateMessage.addData(new WxMpTemplateData("updateAt", cityInfo.getString("updateTime"),"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("type", forecast.getJSONObject(0).getString("type") ,"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("high", forecast.getJSONObject(0).getString("high") ,"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("low", forecast.getJSONObject(0).getString("low") ,"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("sunrise", forecast.getJSONObject(0).getString("sunrise") ,"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("sunset", forecast.getJSONObject(0).getString("sunset") ,"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("fx", forecast.getJSONObject(0).getString("fx") ,"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("fl", forecast.getJSONObject(0).getString("fl") ,"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("notice", forecast.getJSONObject(0).getString("notice") ,"#00BFFF"));
            System.out.println(templateMessage.toJson());
        }
        try {
            String s = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Scheduled(cron = "0 30 9 * * ? ")
    public void push1() {
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appID);
        wxStorage.setSecret(appsecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("oyB1H6phb2WXgXjHux5NbgHu70T0")
                .templateId("436Ix8aQSn5ZfOxAcxYI3l1U7uJvrCGpH3GsA4UFk4E")
                .build();

        String mryyUrl = "https://open.iciba.com/dsapi/";
        String mryyRes = HttpUtil.get(mryyUrl);
        JSONObject mryy = JSONObject.parseObject(mryyRes);
        if(mryy!= null ){
            templateMessage.addData(new WxMpTemplateData("notice",mryy.getString("content"),"#00BFFF"));
            templateMessage.addData(new WxMpTemplateData("content",mryy.getString("note"),"#00BFFF"));
            System.out.println(templateMessage.toJson());
        }
        try {
            String s = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }

    }


    @Scheduled(cron = "0 30 9 * * ? ")
    public void push2() {
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId(appID);
        wxStorage.setSecret(appsecret);
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);
        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("oyB1H6phb2WXgXjHux5NbgHu70T0")
                .templateId("zdRuwg5ZvDUuPyEiXzOqMVmZpyp96ooBubXslDtBp-E")
                .build();

        String res = HttpUtil.get("https://api.oick.cn/dutang/api.php");
        templateMessage.addData(new WxMpTemplateData("content", res ,"#00BFFF"));
        try {
            String s = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println("推送失败：" + e.getMessage());
            e.printStackTrace();
        }

    }




    /**
     *	将今日日期转换成yyyy-MM-dd E：2022-09-09 星期四
     */
    public static String format(){
        SimpleDateFormat myFmt3 = new SimpleDateFormat("yyyy-MM-dd E");
        Date now = new Date();
        return myFmt3.format(now);
    }



    public static String getIp2(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }



}
