package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2021/6/24 9:36
 * @since 1.0
 */
@TableName("db_code_connection_user_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbCodeConnectionUserGroupPO extends BaseModelCreateUserTime {

    private static final long serialVersionUID = -2976101858494136551L;
    @TableId(type = IdType.NONE)
    private Long connectionId;

    private Long userGroupId;

}
