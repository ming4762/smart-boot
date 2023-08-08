package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 变更详细信息
 * @author zhongming4762
 * 2023/7/31 17:57
 */
@Getter
@Setter
@TableName("smart_change_log_detail")
public class SmartChangeLogDetailPO extends BaseModel {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long logId;

    private String changeField;

    private String beforeValue;

    private String afterValue;
}
