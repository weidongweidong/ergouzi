package com.ergouzi.eurekaproducer.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @description: 定时任务
 * @author: chenweidong
 * @create: 2021-08-05 16:03
 **/

@RestController
public class task {

    @GetMapping("/price")
    public void getPrice() throws IOException {
        String url  = "https://www.qqw21.com/katongtouxiang/";
        Document doc = Jsoup.connect(url).get();
        Elements select = doc.select(".lazy");

        String src = select.attr("src");
        System.out.print(src);

    }
}
