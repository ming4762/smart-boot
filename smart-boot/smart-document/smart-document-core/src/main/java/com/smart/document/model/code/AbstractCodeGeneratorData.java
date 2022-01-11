package com.smart.document.model.code;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author ShiZhongMing
 * 2021/8/20 13:13
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public abstract class AbstractCodeGeneratorData {

    @ApiModelProperty(value = "宽度", example = "200")
    private Integer width;

    @ApiModelProperty(value = "高度", example = "100")
    private Integer height;

    @ApiModelProperty(value = "边距", example = "10")
    private Integer margin;
}
