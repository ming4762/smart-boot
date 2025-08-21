package com.smart.framework.tool.database.pojo.dto;

import lombok.*;

import java.io.Serializable;

/**
 * SQL查询列
 * @author shizhongming
 * 2025/8/13 17:37
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SmartSelectColumn implements Serializable {

    private String column;

    private String left;

    private String right;
}
