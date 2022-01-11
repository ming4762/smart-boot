package com.smart.document.model.code;

import com.smart.document.constants.CodeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "条形码生成参数", parent = AbstractCodeGeneratorData.class)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BarcodeGeneratorData extends AbstractCodeGeneratorData {

    @ApiModelProperty(value = "条形码内容", required = true)
    @NotNull(message = "条形码内容不能为空")
    private String content;

    @ApiModelProperty(value = "条形码类型", example = "CODE_39")
    private CodeFormat format;

    @ApiModelProperty(value = "是否显示内容", dataType = "Boolean", example = "true")
    private Boolean showWord;

    @ApiModelProperty(value = "文字高度", example = "25")
    private Integer wordHeight;

    @ApiModelProperty(value = "文字顶部边距", example = "14")
    private Integer wordTopMargin;

    @ApiModelProperty(value = "字体设置")
    private CodeFont font;

    /**
     * 文字字体
     */
    @Getter
    @Setter
    @ApiModel(value = "条形码字体设置", parent = BarcodeGeneratorData.class)
    public static class CodeFont {

        @ApiModelProperty(value = "字体名字")
        private String name;

        @ApiModelProperty(value = "字体样式")
        private Integer style;

        @ApiModelProperty(value = "字体大小")
        private Integer size;

        @ApiModelProperty(value = "是否自动空格,字体长度太小会自动用空格填充", example = "true")
        private Boolean autoSpace;

        public Font createFont() {
            return new Font(this.name, this.style, this.size);
        }
    }
}
