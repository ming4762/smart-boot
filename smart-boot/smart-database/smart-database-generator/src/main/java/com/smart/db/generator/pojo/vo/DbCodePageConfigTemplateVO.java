package com.smart.db.generator.pojo.vo;

import com.smart.db.generator.model.DbCodePageConfigPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2021/5/8 13:06
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCodePageConfigTemplateVO extends DbCodePageConfigPO {
    private static final long serialVersionUID = 2977613076184347745L;

    public DbCodePageConfigTemplateVO() {
        this.idAnnotation = Boolean.FALSE;
    }

    /**
     * 是否使用ID注解（mybatis）
     */
    private Boolean idAnnotation;
}
