package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ShiZhongMing
 * 2021/5/7 17:14
 * @since 1.0
 */
@Getter
@Setter
@TableName("db_code_template")
public class DbCodeTemplatePO extends BaseModelUserTime {
    private static final long serialVersionUID = 8967841209043755314L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long templateId;

    /**
     * 字典类型
     */
    private String templateType;

    private String name;

    private String remark;

    private String language;

    private String template;

    /**
     * 文件名后缀
     */
    private String filenameSuffix;

    @TableLogic
    private Boolean deleteYn;

    private Long groupId;
}
