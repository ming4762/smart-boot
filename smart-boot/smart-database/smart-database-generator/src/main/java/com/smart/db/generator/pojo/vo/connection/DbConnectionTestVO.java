package com.smart.db.generator.pojo.vo.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据库连接测试结果
 * @author ShiZhongMing
 * 2021/7/28 11:27
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class DbConnectionTestVO {

    private Boolean result;

    private String message;
}
