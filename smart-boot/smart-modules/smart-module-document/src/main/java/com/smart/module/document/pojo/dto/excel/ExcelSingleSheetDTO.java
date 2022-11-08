package com.smart.module.document.pojo.dto.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * excel单sheet参数
 * @author ShiZhongMing
 * 2021/8/13 21:58
 * @since 1.0
 */
@Getter
@Setter
@Schema(name = "填充单一sheetExcel(带有条形码&二维码)参数")
@ToString
public class ExcelSingleSheetDTO {

    /**
     * 模板编码
     */
    @NotNull(message = "模板编码不能为空")
    @Schema(name = "模板编码", required = true)
    private String templateCode;

    /**
     * 填充数据
     */
    @Schema(name = "填充数据")
    private ExcelFillWithCodeData data;
}
