package com.smart.module.document.pojo.dto.converter;

import com.smart.document.constants.DocumentFormatEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "文档转换参数")
public class DocumentConvertDTO {

    @Schema(name = "文件")
    private MultipartFile file;

    @Schema(name = "源格式")
    private DocumentFormatEnum fromFormat;

    @Schema(name = "目标格式")
    private DocumentFormatEnum toFormat;
}
