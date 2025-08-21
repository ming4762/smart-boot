package com.smart.framework.tool.database.pojo.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.sf.jsqlparser.statement.Statement;

import java.io.Serializable;
import java.util.List;

/**
 * SQL解析结果
 * @author shizhongming
 * 2025/8/13 17:02
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class SmartSqlInfo implements Serializable {

    /**
     * 原始SQL
     */
    private String sql;

    /**
     * 解析后的SQL语句
     */
    private Statement statement;

    /**
     * 占位符列表
     */
    private List<String> placeholderList;

}
