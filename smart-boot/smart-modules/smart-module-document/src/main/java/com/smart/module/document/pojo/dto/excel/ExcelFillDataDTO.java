package com.smart.module.document.pojo.dto.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @author ShiZhongMing
 * 2021/8/16 20:45
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(name = "excel填充数据")
public class ExcelFillDataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3262362513110914027L;
    @NotNull(message = "模板编码不能为空")
    private String templateCode;

    private Map<String, Object> data;
}
