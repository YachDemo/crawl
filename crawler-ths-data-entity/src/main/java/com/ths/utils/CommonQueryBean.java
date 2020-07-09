package com.ths.utils;

import lombok.Data;

/**
 * 通用分页查询条件Bean
 *
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午12:16
 **/
@Data
public class CommonQueryBean {
    private Integer pageNum;
    private Integer pageSize;
    private Integer start;
    private String sort;
    private String order;
    private Integer page;
    private Integer rows;
}
