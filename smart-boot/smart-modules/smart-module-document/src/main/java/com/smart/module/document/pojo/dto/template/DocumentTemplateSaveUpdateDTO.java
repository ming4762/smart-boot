package com.smart.module.document.pojo.dto.template;

import com.smart.module.document.constants.TemplateIdentEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author shizhongming
 * 2021/8/14 8:16 下午
 */
@ToString
@Getter
@Setter
@ApiModel("模板上传参数")
public class DocumentTemplateSaveUpdateDTO {

    @ApiModelProperty("模板文件")
    private MultipartFile file;

    @ApiModelProperty(value = "模板编码", required = true)
    @NotNull(message = "模板编码不能为空")
    private String templateCode;

    @ApiModelProperty(value = "模板名字", required = true)
    @NotNull(message = "模板名字不能为空")
    private String templateName;

    @ApiModelProperty(value = "模板标识", required = true)
    @NotNull(message = "模板标识不能为空")
    private TemplateIdentEnum ident;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("序号")
    private Integer seq;

    @ApiModelProperty("模板ID，更新时必须有该字段")
    private Long templateId;
}
