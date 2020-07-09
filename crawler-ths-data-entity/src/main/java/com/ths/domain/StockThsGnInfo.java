package com.ths.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author YanCh
 * @version v1.0
 * Create by 2020-07-08 下午12:15
 **/
/**
    * 同花顺的概念个股数据
    */
@Data
@TableName(value = "stock_ths_gn_info")
public class StockThsGnInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 概念名
     */
    @TableField(value = "gn_name")
    private String gnName;

    /**
     * 概念的code
     */
    @TableField(value = "gn_code")
    private String gnCode;

    /**
     * 股票code
     */
    @TableField(value = "stock_code")
    private String stockCode;

    /**
     * 股票名
     */
    @TableField(value = "stock_name")
    private String stockName;

    /**
     * 现价
     */
    @TableField(value = "stock_price")
    private BigDecimal stockPrice;

    /**
     * 涨跌幅（单位百分比：%）
     */
    @TableField(value = "stock_change")
    private BigDecimal stockChange;

    /**
     * 涨跌价格
     */
    @TableField(value = "stock_change_price")
    private BigDecimal stockChangePrice;

    /**
     * 涨跌速（单位百分比：%）
     */
    @TableField(value = "stock_change_speed")
    private BigDecimal stockChangeSpeed;

    /**
     * 换手率（单位百分比：%）
     */
    @TableField(value = "stock_handover_scale")
    private BigDecimal stockHandoverScale;

    /**
     * 量比
     */
    @TableField(value = "stock_liang_bi")
    private BigDecimal stockLiangBi;

    /**
     * 振幅（单位百分比：%）
     */
    @TableField(value = "stock_amplitude")
    private BigDecimal stockAmplitude;

    /**
     * 成交额(单位：万)
     */
    @TableField(value = "stock_deal_amount")
    private BigDecimal stockDealAmount;

    /**
     * 流通股票量（单位:万）
     */
    @TableField(value = "stock_flow_stock_number")
    private BigDecimal stockFlowStockNumber;

    /**
     * 流通市值（单位：万）
     */
    @TableField(value = "stock_flow_makert_value")
    private BigDecimal stockFlowMakertValue;

    /**
     * 市盈率
     */
    @TableField(value = "stock_market_ttm")
    private BigDecimal stockMarketTtm;

    /**
     * 发起抓取时间
     */
    @TableField(value = "crawler_time")
    private String crawlerTime;

    /**
     * 当前抓取的版本
     */
    @TableField(value = "crawler_version")
    private String crawlerVersion;

    /**
     * 扩展数据
     */
    @TableField(value = "some_ext")
    private String someExt;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}