package com.smart.system.pojo.dto.i18n;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 国际化组保存/修改实体
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@Schema(title = "国际化组保存/修改实体")
@Getter
@Setter
@ToString
public class SysI18nGroupSaveUpdateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3457561915135780546L;

    @NotNull(message = "{system.i18n.group.group_name_null}")
    private String groupName;

    @NotNull(message = "{system.i18n.group.seq_null}")
    private Integer seq;

    private Long groupId;

    private Long parentId;
}
