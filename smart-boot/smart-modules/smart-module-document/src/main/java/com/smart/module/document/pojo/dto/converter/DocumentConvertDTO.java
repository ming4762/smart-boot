package com.smart.module.document.pojo.dto.converter;

import com.smart.document.constants.DocumentFormatEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ShiZhongMing
 * 2021/8/27 14:09
 * @since 1.0
 */
@ToString
@Getter
@Setter
@ApiModel("文档转换参数")
public class DocumentConvertDTO {

    @ApiModelProperty("文件")
    private MultipartFile file;

    @ApiModelProperty("源格式")
    private DocumentFormatEnum fromFormat;

    @ApiModelProperty("目标格式")
    private DocumentFormatEnum toFormat;
}
