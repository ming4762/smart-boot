package com.smart.db.generator.pojo.vo;

import com.smart.db.generator.model.DbCodeMainPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ShiZhongMing
 * 2021/5/7 10:36
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCodeMainListVO extends DbCodeMainPO {
    private static final long serialVersionUID = 539767754724258473L;

    /**
     * 数据库连接名字
     */
    private String connectionName;

    private String tableRemarks;

}
