package com.smart.document.model.code;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

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
public class QrcodeGeneratorData extends AbstractCodeGeneratorData implements Serializable {

    @Serial
    private static final long serialVersionUID = -7354137234049035062L;
    @Schema(title = "条形码内容", required = true)
    @NotNull(message = "条形码内容不能为空")
    private String content;
}
