package com.smart.smc.inter.qingdaoport.dto.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 云港通船舶计划结果
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025-12-10 15:48
 * @since 5.0.0
 */
@Getter
@Setter
public class QingdaoPortShipPlanResult implements Serializable {


    /**
     * IMO号
     */
    private String cimo;

    /**
     * 英文船名
     */
    private String vesselNameEn;

    /**
     * 船舶代码
     */
    private String cmdm;

    /**
     * 进口航次
     */
    private String imVoyageNo;

    /**
     * 出口航次
     */
    private String exVoyageNo;

    /**
     * 航线代码
     */
    private String routeCode;

    /**
     * 靠泊码头
     */
    private String mtdm;

    /**
     * 承运人
     */
    private String carrier;

    /**
     * 计划靠泊时间(yyyy-MM-dd hh:mm:ss)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private ZonedDateTime eta;

    /**
     * 计划离泊时间(yyyy-MM-dd hh:mm:ss)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private ZonedDateTime etd;

    /**
     * 实际抵港时间(yyyy-MM-dd hh:mm:ss)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private ZonedDateTime arrivalTime;

    /**
     * 实际离港时间(yyyy-MM-dd hh:mm:ss)
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private ZonedDateTime departureTime;

    /**
     * 集港开始时间(yyyy-MM-dd hh:mm:ss)
     */
    @JsonProperty(value = "sxkssj")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private ZonedDateTime incomeCtnTimeFrom;
    /**
     * 集港结束时间(yyyy-MM-dd hh:mm:ss)
     */
    @JsonProperty(value = "sxjssj")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private ZonedDateTime incomeCtnTimeTo;

    /**
     * 中文船名
     */
    private String vesselNameCn;

    /**
     * 位置
     */
    private String location;

    /**
     * 进口代理
     */
    private String importAgent;

    /**
     * 抵港状态
     */
    private String arrivalStatus;

    /**
     * 泊位
     */
    private String berth;

    /**
     * 内外贸
     */
    private String nwm;
}
