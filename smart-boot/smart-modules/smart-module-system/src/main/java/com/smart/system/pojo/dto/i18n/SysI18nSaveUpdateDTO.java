package com.smart.system.pojo.dto.i18n;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 保存添加国际化信息
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@Getter
@Setter
@ToString
public class SysI18nSaveUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3941780796398979844L;

    private Long i18nId;

    @NotNull(message = "平台不能为空")
    private String platform;

    @NotNull(message = "I18N编码不能为空")
    private String i18nCode;

    @NotNull(message = "分组ID不能为空")
    private Long groupId;

    @NotNull(message = "序号不能为空")
    private Integer seq;

    private String remark;
}
