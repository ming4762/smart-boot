package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
@TableName("sys_i18n_group")
@Getter
@Setter
public class SysI18nGroupPO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = -1484548545255008582L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long groupId;

    private String groupName;

    /**
     * 上级ID
     */
    private Long parentId;

    private Integer seq;
}
