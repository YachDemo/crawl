package com.ths.service.impl;

import com.ths.service.IThsParseHtmlService;
import com.ths.service.ThsGnCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午4:49
 **/
@Service
@Slf4j
public class ThsGnCrawlServiceImpl implements ThsGnCrawlService {
    /**
     * 同花顺全部概念板块url
     */
    private final static String GN_URL = "http://q.10jqka.com.cn/gn/";

    @Autowired
    IThsParseHtmlService thsParseHtmlService;

    @Override
    public List<Map<String, String>> ThsGnCrawlListUrl() {
        System.setProperty("webdriver.chrome.driver", "/home/yanch/workCode/java/crawl/crawler-ths-data-crawl/src/main/resources/chromedriver");
        ChromeOptions options = new ChromeOptions();
        WebDriver webDriver = new ChromeDriver(options);
        try {
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            webDriver.get(GN_URL);
            Thread.sleep(1000L);
            String gnWindow = webDriver.getWindowHandle();
            String thsGnHtml = webDriver.getPageSource();
            log.info("获取同花顺url:[{}]的html为:/n{}", GN_URL, thsGnHtml);
            return thsParseHtmlService.parseGnHtmlReturnGnUrlList(thsGnHtml);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.close();
            webDriver.quit();
        }
        return null;
    }
}
