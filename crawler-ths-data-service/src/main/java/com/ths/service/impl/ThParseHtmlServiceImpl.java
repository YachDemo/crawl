package com.ths.service.impl;

import com.ths.service.IThsParseHtmlService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午12:22
 **/
@Service
public class ThParseHtmlServiceImpl implements IThsParseHtmlService {

    @Override
    public List<Map<String, String>> parseGnHtmlReturnGnUrlList(String html) {
        if (StringUtils.isBlank(html)) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements cateItemsFromClass = document.getElementsByClass("cate_items");
        cateItemsFromClass.forEach(element -> {
            Elements as = element.getElementsByTag("a");
            as.forEach(a -> {
                String gnUrl = a.attr("href");
                String name = a.text();
                Map<String, String> map = new HashMap<>();
                map.put("url", gnUrl);
                map.put("gnName", name);
                list.add(map);
            });
        });
        return list;
    }
}
