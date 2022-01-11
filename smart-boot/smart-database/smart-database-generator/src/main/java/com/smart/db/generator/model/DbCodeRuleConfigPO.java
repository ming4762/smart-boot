package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import com.smart.db.generator.constants.RuleIdentEnum;
import com.smart.db.generator.constants.RuleTriggerEnum;
import com.smart.db.generator.constants.RuleTypeEnum;
import com.smart.db.generator.mybatis.type.RuleTriggerTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/6/11 8:28
 * @since 1.0
 */
@Getter
@Setter
@TableName(value = "db_code_rule_config", autoResultMap = true)
public class DbCodeRuleConfigPO extends BaseModel {
    private static final long serialVersionUID = -478223145425048284L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private RuleTypeEnum ruleType;

    @TableField(typeHandler = RuleTriggerTypeHandler.class)
    private List<RuleTriggerEnum> ruleTrigger;

    private Long len;

    private Long max;

    private Long min;

    private String message;

    private String pattern;

    private Integer seq;

    private RuleIdentEnum ident;

    private Long relationId;
}
