package com.ths.service;

import java.util.List;
import java.util.Map;

/**
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午5:03
 **/
public interface ThsGnDetailCrawlService {

    void putAllArrayBlockingQueue(List<Map<String, String>> list);

    void consumeCrawlerGnDetailData(int threadNumber);
}
