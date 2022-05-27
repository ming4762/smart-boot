package com.smart.document.model.code;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(subTypes = {
        BarcodeGeneratorData.class
})
public abstract class AbstractCodeGeneratorData {

    @Schema(title = "宽度", example = "200")
    private Integer width;

    @Schema(title = "高度", example = "100")
    private Integer height;

    @Schema(title = "边距", example = "10")
    private Integer margin;
}
