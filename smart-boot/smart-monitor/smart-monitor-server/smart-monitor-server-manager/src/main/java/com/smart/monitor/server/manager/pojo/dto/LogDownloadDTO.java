package com.smart.monitor.server.manager.pojo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志下载参数
 * @author ShiZhongMing
 * 2022/3/4
 * @since 2.0.0
 */
@ToString
@Getter
@Setter
public class LogDownloadDTO {

    private String level;

    private Long startTime;

    private Long endTime;

    private String applicationCode;

    private String clientId;
}
