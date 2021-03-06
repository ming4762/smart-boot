package com.smart.module.document.pojo.dto.excel;

import com.smart.document.model.code.BarcodeGeneratorData;
import com.smart.document.model.code.QrcodeGeneratorData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author ShiZhongMing
 * 2021/8/13 21:34
 * @since 1.0
 */
@Getter
@Setter
@ApiModel("excel填充数据（带条形码&二维码）")
public class ExcelFillWithCodeData {

    @ApiModelProperty("填充数据")
    private Map<String, Object> data;

    @ApiModelProperty("条形码")
    private Map<String, BarcodeGeneratorData> barcodeMap;

    @ApiModelProperty("二维码")
    private Map<String, QrcodeGeneratorData> qrcodeMap;
}
