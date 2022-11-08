package com.smart.document.model.code;

import com.smart.document.constants.CodeFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.awt.*;


/**
 * 条形码生成参数
 * @author ShiZhongMing
 * 2021/8/13 14:16
 * @since 1.0
 */
@Getter
@Setter
@Schema(name = "条形码生成参数", subTypes = {BarcodeGeneratorData.CodeFont.class})
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BarcodeGeneratorData extends AbstractCodeGeneratorData {

    @Schema(name = "条形码内容", required = true)
    @NotNull(message = "条形码内容不能为空")
    private String content;

    @Schema(name = "条形码类型", example = "CODE_39")
    private CodeFormat format;

    @Schema(name = "是否显示内容", example = "true")
    private Boolean showWord;

    @Schema(name = "文字高度", example = "25")
    private Integer wordHeight;

    @Schema(name = "文字顶部边距", example = "14")
    private Integer wordTopMargin;

    @Schema(name = "字体设置")
    private CodeFont font;

    /**
     * 文字字体
     */
    @Getter
    @Setter
    @Schema(name = "条形码字体设置")
    public static class CodeFont {

        @Schema(name = "字体名字")
        private String name;

        @Schema(name = "字体样式")
        private Integer style;

        @Schema(name = "字体大小")
        private Integer size;

        @Schema(name = "是否自动空格,字体长度太小会自动用空格填充", example = "true")
        private Boolean autoSpace;

        public Font createFont() {
            return new Font(this.name, this.style, this.size);
        }
    }
}
