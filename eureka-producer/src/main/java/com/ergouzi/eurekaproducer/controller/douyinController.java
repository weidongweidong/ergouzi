package com.ergouzi.eurekaproducer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ergouzi.eurekaproducer.utils.HttpUtils;
import com.ergouzi.eurekaproducer.utils.ResultUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://v3-web.douyinvod.com/a08f4e12ec24283734480b232b5c7e69/611534c3/video/tos/cn/tos-cn-ve-15/a3676bc07b5b4543a64976bd3bf02948/?a=6383&br=3190&bt=3190&cd=0%7C0%7C0&ch=26&cr=0&cs=0&cv=1&dr=0&ds=4&er=&ft=0J.120FFckag3&l=021628776046659fdbddc0100fff0030a10241b00000023abdc62&lr=all&mime_type=video_mp4&net=0&pl=0&qs=0&rc=am05cW9ka3RkNTMzNGkzM0ApOmY3aGg6OWQ7N2ZmOTc7aGczY2dzcDIyYnBgLS1kLWFzczM0MDY2LjIuLTI1YmAyYl86Yw%3D%3D&vl=&vr=
 */

/**
 * @description: 抖音解析
 * @author: chenweidong
 * @create: 2021-08-05 16:00
 **/
@RestController
public class douyinController {

    @RequestMapping("dyresolving")
    public JSONObject DYresolving(String url) throws IOException {
        try{
            if("".equals(url)){
                return ResultUtil.error("url 为空 ");
            }
            List<String> extractedUrls = extractUrls(url);
            if(extractedUrls ==null || extractedUrls.size() == 0){
                return ResultUtil.error("url 解析失败");
            }
            JSONObject jsonObject = fun1(extractedUrls.get(0));
            if(jsonObject ==null){
                return ResultUtil.error("解析抖音错误");
            }
            return ResultUtil.success("success", jsonObject);
        }catch (Exception e){
            return ResultUtil.error("视频解析错误 ");
        }
    }


    public static List<String> extractUrls(String text)
    {
        try{
            List<String> containedUrls = new ArrayList<String>();
            String urlRegex ="((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
            Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
            Matcher urlMatcher = pattern.matcher(text);
            while (urlMatcher.find())
            {
                containedUrls.add(text.substring(urlMatcher.start(0),
                        urlMatcher.end(0)));
            }
            return containedUrls;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public static <jsonObject> JSONObject fun1(String url) throws IOException {
        JSONObject jsonObject = new JSONObject();
        try{
            //    Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").timeout(5000).get();
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").get();
            Elements select = doc.select("#RENDER_DATA");
            String json = select.toString().replace("</script>","").replace("<script id=\"RENDER_DATA\" type=\"application/json\">","");
            if(!"".equals(json)){
                String str = URLDecoder.decode(json,"UTF-8");
                JSONObject parse = JSON.parseObject(str);
                JSONObject C_14 = parse.getJSONObject("C_14");
                JSONObject detail = C_14.getJSONObject("aweme").getJSONObject("detail");
                String desc = detail.getString("desc");
                JSONObject palyUrl = (JSONObject) detail.getJSONObject("video").getJSONArray("playAddr").get(1);
                String o = palyUrl.getString("src");
                jsonObject.put("DESC",desc);
                jsonObject.put("URL","https:"+o);
                return jsonObject;
            }
            String location = doc.location();
            String rege  = "/\\d+/";
            Pattern pattern = Pattern.compile(rege, Pattern.CASE_INSENSITIVE);
            Matcher urlMatcher = pattern.matcher(location);
            List<String> containedUrls = new ArrayList<String>();
            while (urlMatcher.find())
            {
                containedUrls.add(location.substring(urlMatcher.start(0),
                        urlMatcher.end(0)));
            }
            if(containedUrls.size() == 0){
                return null;
            }
            String id = containedUrls.get(0);
            String substring = id.substring(1, id.length() - 1);
            String img_url = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=" + substring;
            Document doc1 = Jsoup.connect(img_url).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").get();
            Element body = doc1.body();
            String s = body.toString().replace("<body>","").replace("</body>","");
            JSONObject jsonObject1 = JSON.parseObject(s);
            JSONArray item_list = jsonObject1.getJSONArray("item_list");
            JSONObject data = item_list.getJSONObject(0);
            jsonObject.put("DESC",data.getString("desc"));
            JSONArray images = data.getJSONArray("images");
            JSONArray array = new JSONArray();
            for(int i=0 ;i < images.size() ; i++){
                JSONObject jsonObject2 = images.getJSONObject(i);
                JSONArray url_list = jsonObject2.getJSONArray("url_list");
                array.add(url_list.get(0));
            }
            jsonObject.put("URL" ,array);
            return jsonObject;

        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
