package com.ths.service;

import java.util.List;
import java.util.Map;

/**
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午12:21
 **/
public interface IThsParseHtmlService {

    /**
     * 解析同花顺概念板块的Html页面：http://q.10jqka.com.cn/gn/
     * 返回所有概念板块的url地址
     *
     * @param html
     * @return
     */
    List<Map<String, String>> parseGnHtmlReturnGnUrlList(String html);

}
