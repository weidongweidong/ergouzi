package com.ergouzi.eurekaproducer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class EurekaProducerApplicationTests {
	@Test
	void aa(){
		String url = "https://www.iesdouyin.com/share/video/6994353174702279976/?region=CN&mid=6961018344162003742&u_code=icg2j76j&titleType=title&did=MS4wLjABAAAAm9748VONuKflTSpV7Q8IrMlHTmIDzd7Nssr_5jIZOZ9IRshWuyO4IKKfpb4Io7lJ&iid=MS4wLjABAAAAgOhbQdQft0U2OwAQ6BoJn-NrCEf59rd27IaVMg7dsp88nOLAbXyxGp1FjAzHXG9x&with_sec_did=1&schema_type=37&timestamp=1628771199&utm_campaign=client_share&app=aweme&utm_medium=ios&tt_from=copy&utm_source=copy";
		String rege  = "/\\d+/";
		Pattern pattern = Pattern.compile(rege, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(url);
		List<String> containedUrls = new ArrayList<String>();
		while (urlMatcher.find())
		{
			containedUrls.add(url.substring(urlMatcher.start(0),
					urlMatcher.end(0)));
		}
		System.out.println(containedUrls.toString());
	}

	@Test
	void contextLoads() throws Exception {

		String url  = "https://www.qqw21.com/katongtouxiang/";
		Document doc = Jsoup.connect(url).get();
		Elements lazy1 = doc.getElementsByClass("lazy");
		Element body = doc.body();
//		System.out.println(body);
		Elements select1 = body.select(".touxiang_list li img");
//		System.out.println(select1);
		for(int i=0; i< select1.size(); i++){
			Element element = select1.get(i);
			String src = element.attr("src");
			String alt = element.attr("alt");
			System.out.print(src);
			System.out.println(alt);
			download(src ,alt+ ".jpg" );
		}

	}

	public static void download(String urlString, String filename) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		OutputStream os = new FileOutputStream(filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}



	@Test
	public void text(){
		JSONObject addressObj = new JSONObject();

		addressObj.put("省份","北京");
		addressObj.put("城市","北京");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name","chenweidong");
		jsonObject.put("address" , addressObj);
		System.out.println(jsonObject);
	}
}
