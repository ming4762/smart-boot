package com.smart.monitor.server.manager.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2022/3/30 13:50
 * @since 1.0
 */
@ToString
@Getter
@Setter
@Schema(name = "查询client id dto")
public class ClientIdQueryDTO implements Serializable {
    private static final long serialVersionUID = 6167882977654809522L;

    @Schema(name = "应用名称")
    private String applicationName;
}
