package com.smart.system.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author jackson
 * 2020/1/30 2:03 下午
 */
@ToString
@Getter
@Setter
@Schema(title = "用户所在用户组参数实体")
public class UserUserGroupSaveDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6508219832444270076L;

    @Schema(title = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(title = "用户组ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用户组ID不能为空")
    @Length(min = 1, message = "用户组ID不能为空")
    private List<Long> groupIdList;
}
