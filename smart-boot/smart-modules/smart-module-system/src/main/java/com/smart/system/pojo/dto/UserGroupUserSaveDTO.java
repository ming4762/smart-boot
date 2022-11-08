package com.smart.system.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 保存用户组的用户DTO
 * @author jackson
 * 2020/1/30 12:54 下午
 */
@ToString
@Getter
@Setter
@Tag(name = "保存用户组的用户实体")
public class UserGroupUserSaveDTO implements Serializable {
    private static final long serialVersionUID = 3856463051638844582L;

    @NotNull(message = "用户组ID不能为空")
    @Schema(name = "用户组ID", required = true)
    private Long groupId;

    @NotNull(message = "用户ID不能为空")
    @Schema(name = "用户ID集合", required = true)
    private List<Long> userIdList;
}
