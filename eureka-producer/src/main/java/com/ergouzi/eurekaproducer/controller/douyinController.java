package com.ergouzi.eurekaproducer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ergouzi.eurekaproducer.utils.HttpUtils;
import com.ergouzi.eurekaproducer.utils.ResultUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;
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

            JSONArray urls = jsonObject.getJSONArray("URL");
            JSONArray array = new JSONArray();
            String path = "static/";
            for(int i=0 ;i < urls.size();i++){
                String urlString = (String) urls.get(i);
                if(jsonObject.getString("TYPE").equals("images")){
                    String id = jsonObject.getString("ID");
                    File file = new File(path + "/"+ id +"_"+ i + ".jpg");
                    if(!file.exists()){
                        download(urlString,  id +"_"+ i + ".jpg",path);
                    }
                    array.add("https://cloud.chenweidong.top/producer/images/"+ id +"_"+ i + ".jpg");
                }else{
                    String id = jsonObject.getString("ID");
                    File file = new File(path + "/"+ id +"_"+ i + ".mp4");
                    if(!file.exists()){
                        download(urlString, id +"_"+ i + ".mp4",path);
                    }
                    array.add("https://cloud.chenweidong.top/producer/images/"+id +"_"+ i + ".mp4");
                }
            }
            jsonObject.put("URL",array);
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

            Document doc = Jsoup.connect(url).ignoreContentType(true).timeout(5000).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").referrer("").get();
            String location = doc.location();
            String rege  = "/\\d+[/|?]";
            Pattern pattern = Pattern.compile(rege, Pattern.CASE_INSENSITIVE);
            Matcher urlMatcher = pattern.matcher(location);
            List<String> containedUrls = new ArrayList<String>();
            while (urlMatcher.find())
            {
                containedUrls.add(location.substring(urlMatcher.start(0),
                        urlMatcher.end(0)));
            }

            if(containedUrls.size() == 0){
                return new JSONObject();
            }




            String id = containedUrls.get(0);
            id = id.substring(1, id.length() - 1);
            String img_url = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=" + id ;
            Document doc1 = Jsoup.connect(img_url).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").get();
            Element body = doc1.body();
            String s = body.toString().replace("<body>","").replace("</body>","");
            JSONObject jsonObject1 = JSON.parseObject(s);
            JSONArray item_list = jsonObject1.getJSONArray("item_list");
            JSONObject data = item_list.getJSONObject(0);
            jsonObject.put("DESC",data.getString("desc"));
            JSONArray images = data.getJSONArray("images");
            JSONObject video = data.getJSONObject("video");
            JSONArray array = new JSONArray();
            if(images!=null && images.size() > 0){
                for(int i=0 ;i < images.size() ; i++){
                    JSONObject jsonObject2 = images.getJSONObject(i);
                    JSONArray url_list = jsonObject2.getJSONArray("url_list");
                    array.add(url_list.get(0));
                }
                jsonObject.put("ID",id);
                jsonObject.put("URL" ,array);
                jsonObject.put("TYPE","images");
            }
//            else if(video != null  && video.getJSONObject("play_addr")!=null){
//
//                JSONObject play_addr = video.getJSONObject("play_addr");
//                JSONArray url_list = play_addr.getJSONArray("url_list");
//                if(url_list.size() > 0){
//                    String videoUrl = url_list.getString(0);
//                    JSONArray array1 = new JSONArray();
//                    array1.add(videoUrl);
//                    jsonObject.put("ID",id);
//                    jsonObject.put("URL",array1);
//                    jsonObject.put("TYPE","video");
//                }
//            }
            else{
                url = "https://api.missuo.me/douyin?url=" + url;
                String request = HttpUtils.getRequest(url);
                JSONObject result = JSON.parseObject(request);
                if(result.getInteger("code") == 200 ){
                    jsonObject.put("ID", result.getString("id"));
                    JSONArray array1 = new JSONArray();
                    array1.add(result.getString("mp4"));
                    jsonObject.put("URL",array1);
                    jsonObject.put("TYPE", "video");
                }
            }

            return jsonObject;

        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }





    /**
     *
     * @param urlString 需要下载图片的路径
     * @param filename  下载后图片的命名
     * @param savePath  下载到那个文件夹下
     * @throws Exception
     */
    public static void download(String urlString, String filename,String savePath) throws Exception {
        try {
            // 构造URL
            urlString = urlString.replace("https","http").replaceAll("amp;","").replace("253D","3D");
            URL url = new URL(urlString);
            // 打开连接
//            Connection conn = Jsoup.connect(urlString).timeout(5000);
//            conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.header("Accept-Encoding", "gzip, deflate, sdch");
//            conn.header("Accept-Language", "zh-CN,zh;q=0.8");
//            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
//            conn.referrer("");
//            Document doc = conn.get();
            URLConnection con = url.openConnection();
            System.out.println(con.toString());


            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf=new File(savePath);
            if(!sf.exists()){
                sf.mkdirs();
            }
            System.out.println("保存地址为"+ sf +"/"+ filename );
            OutputStream os = new FileOutputStream(sf +"/"+ filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        } catch (Exception e) {
            System.out.println("报错了" + e);
        }

    }
}
