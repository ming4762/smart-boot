package com.smart.document.model.code;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author ShiZhongMing
 * 2021/8/13 21:26
 * @since 1.0
 */
@Getter
@Setter
@ApiModel(value = "二维码生成参数")
@ToString
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class QrcodeGeneratorData extends AbstractCodeGeneratorData {

    @ApiModelProperty(value = "条形码内容", required = true)
    @NotNull(message = "条形码内容不能为空")
    private String content;
}
