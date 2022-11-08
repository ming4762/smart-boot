package com.smart.system.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author jackson
 * 2020/1/30 2:03 下午
 */
@ToString
@Getter
@Setter
@Tag(name = "用户所在用户组参数实体")
public class UserUserGroupSaveDTO implements Serializable {
    private static final long serialVersionUID = -6508219832444270076L;

    @Schema(name = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(name = "用户组ID", required = true)
    @NotNull(message = "用户组ID不能为空")
    @Length(min = 1, message = "用户组ID不能为空")
    private List<Long> groupIdList;
}
