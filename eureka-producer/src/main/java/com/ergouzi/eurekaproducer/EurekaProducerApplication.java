package com.ergouzi.eurekaproducer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 生产者
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaProducerApplication.class, args);
	}

	@RequestMapping("sayHello")
	public String sayHello(String param){
	    return  "Hello"+ param;
	}

    @RequestMapping("dyresolving")
	public JSONObject DYresolving(String url) throws IOException {
        try{
            List<String> extractedUrls = extractUrls(url);
            JSONObject jsonObject = fun1(extractedUrls.get(0));
            return  jsonObject;
        }catch (Exception e){
            throw e;
        }
    }

    public static List<String> extractUrls(String text)
    {
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
    }

    public static JSONObject fun1(String url) throws IOException {
	    try{
            Document doc = Jsoup.connect(url).get();
            Elements select = doc.select("#RENDER_DATA");
            String json = select.toString().replace("</script>","").replace("<script id=\"RENDER_DATA\" type=\"application/json\">","");
            String str = URLDecoder.decode(json,"UTF-8");
            JSONObject parse = JSON.parseObject(str);
            JSONObject c_12 = parse.getJSONObject("C_12");
            JSONObject detail = c_12.getJSONObject("aweme").getJSONObject("detail");
            String desc = detail.getString("desc");
            JSONObject palyUrl = (JSONObject) detail.getJSONObject("video").getJSONArray("playAddr").get(1);
            String o = palyUrl.getString("src");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("DESC",desc);
            jsonObject.put("URL","https:"+o);
            return jsonObject;
        }catch (Exception e){
            System.out.print(e);
	        throw e;
        }

    }

}
