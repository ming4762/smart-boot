package com.smart.module.document.pojo.dto.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 多sheet参数
 * @author ShiZhongMing
 * 2021/8/13 22:00
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(name = "填充多sheetExcel(带有条形码&二维码)参数")
public class ExcelMultiSheetDTO {

    /**
     * 模板编码
     */
    @NotNull(message = "模板编码不能为空")
    private String templateCode;

    /**
     * sheet名字
     */
    @Schema(name = "sheet名字列表（sheet名字不能与模板sheet名字相同）")
    private List<String> sheetNameList;

    /**
     * 数据列表
     */
    @Schema(name = "填充数据列表", required = true)
    private List<ExcelFillWithCodeData> dataList;
}
