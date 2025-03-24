package com.smart.module.document.pojo.dto.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * excel单sheet参数
 * @author ShiZhongMing
 * 2021/8/13 21:58
 * @since 1.0
 */
@Getter
@Setter
@Schema(title = "填充单一sheetExcel(带有条形码&二维码)参数")
@ToString
public class ExcelSingleSheetDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8030048025271847141L;
    /**
     * 模板编码
     */
    @NotNull(message = "模板编码不能为空")
    @Schema(title = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String templateCode;

    /**
     * 填充数据
     */
    @Schema(title = "填充数据")
    private ExcelFillWithCodeData data;
}
