package com.smart.system.pojo.dto.i18n;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Locale;

/**
 * TODO:i18n
 * @author ShiZhongMing
 * 2021/11/16
 * @since 1.0.7
 */
@Getter
@Setter
@ToString
public class SysI18nItemSaveUpdateDTO implements Serializable {
    private static final long serialVersionUID = 4281677029598942858L;

    @NotNull(message = "国际化ID不能为空")
    private Long i18nId;

    @NotNull(message = "语言不能为空")
    private Locale locale;

    @NotNull(message = "value不能为空")
    private String value;

    private Long i18nItemId;
}
