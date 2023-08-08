package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelCreateUserTime;
import com.smart.module.api.system.constants.SmartChangeLogEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据变更记录表
 * @author zhongming4762
 * 2023/7/31
 */
@Getter
@Setter
@TableName("smart_change_log")
public class SmartChangeLogPO extends BaseModelCreateUserTime {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String ident;

    private Long businessId;

    private String businessData;

    private SmartChangeLogEnum operateType;
}
