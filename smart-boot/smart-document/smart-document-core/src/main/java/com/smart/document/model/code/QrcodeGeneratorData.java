package com.smart.document.model.code;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ShiZhongMing
 * 2021/8/13 21:26
 * @since 1.0
 */
@Getter
@Setter
@Schema(title = "二维码生成参数")
@ToString
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class QrcodeGeneratorData extends AbstractCodeGeneratorData {

    @Schema(title = "条形码内容", required = true)
    @NotNull(message = "条形码内容不能为空")
    private String content;
}
