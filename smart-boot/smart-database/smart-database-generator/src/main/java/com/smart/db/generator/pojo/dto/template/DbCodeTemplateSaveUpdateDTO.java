package com.smart.db.generator.pojo.dto.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/1/25 21:18
 * TODO: 国际化
 */
@Getter
@Setter
@ToString
public class DbCodeTemplateSaveUpdateDTO implements Serializable {

    private Long templateId;

    @NotNull(message = "模板类型不能为空")
    private String templateType;

    @NotNull(message = "模板名称不能为空")
    private String name;

    @NotNull(message = "语言不能为空")
    private String language;

    @NotNull(message = "模板分组不能为空")
    private Long groupId;

    @NotNull(message = "模板内容不能为空")
    private String template;

    private String filenameSuffix;
}
