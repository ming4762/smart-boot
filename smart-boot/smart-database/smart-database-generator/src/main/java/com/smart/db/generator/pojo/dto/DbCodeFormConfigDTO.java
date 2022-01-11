package com.smart.db.generator.pojo.dto;

import com.smart.db.generator.model.DbCodeFormConfigPO;
import com.smart.db.generator.model.DbCodeRelatedTablePO;
import com.smart.db.generator.model.DbCodeRuleConfigPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * FORM 配置信息
 * @author ShiZhongMing
 * 2021/5/13 9:01
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCodeFormConfigDTO extends DbCodeFormConfigPO {

    private static final long serialVersionUID = -6412413400973081963L;

    /**
     * 下拉表格配置信息
     */
    private List<DbCodeRelatedTablePO> selectTableList;

    /**
     * 验证信息
     */
    private List<DbCodeRuleConfigPO> ruleList;
}
