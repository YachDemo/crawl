package com.ths;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ths.mapper")
public class CrawlerThsDataCrawlApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerThsDataCrawlApplication.class, args);
    }

}
