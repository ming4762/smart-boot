package com.smart.module.document.pojo.dto.template;

import com.smart.module.document.constants.TemplateIdentEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2021/8/14 8:16 下午
 */
@ToString
@Getter
@Setter
@Schema(name = "模板上传参数")
public class DocumentTemplateSaveUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2221212581575843191L;
    @Schema(name = "模板文件")
    private transient MultipartFile file;

    @Schema(name = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "模板编码不能为空")
    private String templateCode;

    @Schema(name = "模板名字", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "模板名字不能为空")
    private String templateName;

    @Schema(name = "模板标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "模板标识不能为空")
    private TemplateIdentEnum ident;

    @Schema(name = "备注")
    private String remark;

    @Schema(name = "序号")
    private Integer seq;

    @Schema(name = "模板ID，更新时必须有该字段")
    private Long templateId;
}
