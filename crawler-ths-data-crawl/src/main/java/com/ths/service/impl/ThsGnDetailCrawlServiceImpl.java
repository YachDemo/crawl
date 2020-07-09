package com.ths.service.impl;

import com.ths.domain.StockThsGnInfo;
import com.ths.mapper.StockThsGnInfoMapper;
import com.ths.service.ThsGnDetailCrawlService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午5:03
 **/
@Service
@Slf4j
public class ThsGnDetailCrawlServiceImpl implements ThsGnDetailCrawlService {

    private ArrayBlockingQueue<Map<String, String>> arrayBlockingQueue = new ArrayBlockingQueue<>(1000);

    @Autowired
    StockThsGnInfoMapper stockThsGnInfoMapper;

    @Override
    public void putAllArrayBlockingQueue(List<Map<String, String>> list) {
        if (!CollectionUtils.isEmpty(list)) {
            arrayBlockingQueue.addAll(list);
        }
    }

    @Override
    public void consumeCrawlerGnDetailData(int threadNumber) {
        for (int i = 0; i < threadNumber; i++) {
            log.info("开启第[{}]个线程", i);
            new Thread(new CrawlerGnDataThread()).start();
        }
    }

    class CrawlerGnDataThread implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Map<String, String> map = arrayBlockingQueue.take();
                    String url = map.get("url");
                    String gnName = map.get("gnName");
                    String crawlerDateStr = new SimpleDateFormat("yyyy-MM-dd HH:00:00").format(new Date());

                    System.setProperty("webdriver.chrome.driver", "/home/yanch/workCode/java/crawl/crawler-ths-data-crawl/src/main/resources/chromedriver");
                    ChromeOptions options = new ChromeOptions();
                    WebDriver webDriver = new ChromeDriver(options);
                    try {
                        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                        webDriver.get(url);
                        Thread.sleep(1000L);
                        String oneGnHtml = webDriver.getPageSource();
                        log.info("当前概念：[{}],html数据为[{}]", gnName, oneGnHtml);
                        log.info(oneGnHtml);
                        // TODO 解析并存储数据
                        parseHtmlAndInsertData(oneGnHtml, gnName, crawlerDateStr);
                        clicktoOneGnNextPage(webDriver, oneGnHtml, gnName, crawlerDateStr);
                    } catch (Exception e) {
                        log.error("用chromerDriver抓取数据，出现异常，url为[{}],异常为[{}]", url, e);
                    } finally {
                        webDriver.close();
                        webDriver.quit();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseHtmlAndInsertData(String oneGnHtml, String gnName, String crawlerDateStr) {
        Document document = Jsoup.parse(oneGnHtml);

        Element table = document.getElementsByClass("m-pager-table").get(0);
        Element tBody = table.getElementsByTag("tbody").get(0);
        Elements trs = tBody.getElementsByTag("tr");

        trs.forEach(tr -> {
            try {
                Elements tds = tr.getElementsByTag("td");
                String stockCode = tds.get(1).text();
                String stockName = tds.get(2).text();
                BigDecimal stockPrice = parseValueToBigDecimal(tds.get(3).text());
                BigDecimal stockChange = parseValueToBigDecimal(tds.get(4).text());
                BigDecimal stockChangePrice = parseValueToBigDecimal(tds.get(5).text());
                BigDecimal stockChangeSpeed = parseValueToBigDecimal(tds.get(6).text());
                BigDecimal stockHandoverScale = parseValueToBigDecimal(tds.get(7).text());
                BigDecimal stockLiangBi = parseValueToBigDecimal(tds.get(8).text());
                BigDecimal stockAmplitude = parseValueToBigDecimal(tds.get(9).text());
                BigDecimal stockDealAmount = parseValueToBigDecimal(tds.get(10).text());
                BigDecimal stockFlowStockNumber = parseValueToBigDecimal(tds.get(11).text());
                BigDecimal stockFlowMakertValue = parseValueToBigDecimal(tds.get(12).text());
                BigDecimal stockMarketTtm = parseValueToBigDecimal(tds.get(13).text());
                // 存储数据
                StockThsGnInfo stockThsGnInfo = new StockThsGnInfo();
                stockThsGnInfo.setGnName(gnName);
                stockThsGnInfo.setGnCode(null);
                stockThsGnInfo.setStockCode(stockCode);
                stockThsGnInfo.setStockName(stockName);
                stockThsGnInfo.setStockPrice(stockPrice);
                stockThsGnInfo.setStockChange(stockChange);
                stockThsGnInfo.setStockChangePrice(stockChangePrice);
                stockThsGnInfo.setStockChangeSpeed(stockChangeSpeed);
                stockThsGnInfo.setStockHandoverScale(stockHandoverScale);
                stockThsGnInfo.setStockLiangBi(stockLiangBi);
                stockThsGnInfo.setStockAmplitude(stockAmplitude);
                stockThsGnInfo.setStockDealAmount(stockDealAmount);
                stockThsGnInfo.setStockFlowStockNumber(stockFlowStockNumber);
                stockThsGnInfo.setStockFlowMakertValue(stockFlowMakertValue);
                stockThsGnInfo.setStockMarketTtm(stockMarketTtm);
                stockThsGnInfo.setCrawlerTime(crawlerDateStr);
                stockThsGnInfo.setCrawlerVersion("同花顺概念板块#" + crawlerDateStr);
                stockThsGnInfo.setCreateTime(new Date());
                stockThsGnInfo.setUpdateTime(new Date());
                stockThsGnInfoMapper.insert(stockThsGnInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BigDecimal parseValueToBigDecimal(String value) {
        if (StringUtils.isEmpty(value)) {
            return BigDecimal.ZERO;
        } else if ("--".equals(value)) {
            return BigDecimal.ZERO;
        } else if (value.endsWith("亿")) {
            return new BigDecimal(value.substring(0, value.length() - 1)).multiply(BigDecimal.ONE);
        }
        return new BigDecimal(value);
    }

    public boolean clicktoOneGnNextPage(WebDriver webDriver, String oneGnHtml, String key, String crawlerDateStr) throws InterruptedException {
        // 是否包含下一页
        String pageNumber = includeNextPage(oneGnHtml);
        if (!StringUtils.isEmpty(pageNumber)) {
            WebElement nextPageElement = webDriver.findElement(By.linkText("下一页"));
            webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            nextPageElement.click();
            Thread.sleep(700);
            String nextPageHtml = webDriver.getPageSource();
            log.info("下一页：");
            log.info(nextPageHtml);
            // TODO 解析并存储数据
            parseHtmlAndInsertData(nextPageHtml, key, crawlerDateStr);
            clicktoOneGnNextPage(webDriver, nextPageHtml, key, crawlerDateStr);
        }
        return true;
    }

    public String includeNextPage(String html) {
        Document document = Jsoup.parse(html);
        List<Element> list = document.getElementsByTag("a");
        for (Element element : list) {
            String a = element.text();
            if ("下一页".equals(a)) {
                String pageNumber = element.attr("page");
                return pageNumber;
            }
        }
        return null;
    }
}
