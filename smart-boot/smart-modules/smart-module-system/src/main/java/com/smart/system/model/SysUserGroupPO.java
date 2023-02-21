package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;


/**
 * @author jackson
 * 2020/1/24 2:56 下午
 */
@TableName("sys_user_group")
@Getter
@Setter
@Schema(description = "用户组实体")
public class SysUserGroupPO extends BaseModelUserTime {


    @Serial
    private static final long serialVersionUID = 7943564843713775352L;

    private static final String PERMISSION_GROUP = "sys:userGroup";
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long groupId;

    /**
     * 用户组名称
     */
    @Schema(title = "用户组名称", description = "用户组名称", required = true)
    @NotNull(message = "用户组名称不能为空")
    private String groupName;

    /**
     * 用户组编码
     */
    @Schema(description = "用户组编码", required = true)
    @NotNull(message = "用户组编码不能为空")
    private String groupCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 是否启用
     */
    @Schema(description = "启用")
    private Boolean useYn;
}
