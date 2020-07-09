package com.ths.controller;

import com.ths.service.ThsGnCrawlService;
import com.ths.service.ThsGnDetailCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午4:44
 **/
@RestController
public class CrawlController {
    @Autowired
    ThsGnCrawlService thsGnCrawlService;
    @Autowired
    ThsGnDetailCrawlService thsGnDetailCrawlService;

    @RequestMapping("/test")
    public void test() {
        // 抓取所有概念板块的url
        List<Map<String, String>> list = thsGnCrawlService.ThsGnCrawlListUrl();
        // 放入阻塞队列
        thsGnDetailCrawlService.putAllArrayBlockingQueue(list);
        // 对线程抓取
        thsGnDetailCrawlService.consumeCrawlerGnDetailData(1);
    }

}
