package com.smart.module.document.pojo.dto.excel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
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
@Schema(title = "填充多sheetExcel(带有条形码&二维码)参数")
public class ExcelMultiSheetDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4694477637074889338L;
    /**
     * 模板编码
     */
    @NotNull(message = "模板编码不能为空")
    private String templateCode;

    /**
     * sheet名字
     */
    @Schema(title = "sheet名字列表（sheet名字不能与模板sheet名字相同）")
    private List<String> sheetNameList;

    /**
     * 数据列表
     */
    @Schema(title = "填充数据列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ExcelFillWithCodeData> dataList;
}
