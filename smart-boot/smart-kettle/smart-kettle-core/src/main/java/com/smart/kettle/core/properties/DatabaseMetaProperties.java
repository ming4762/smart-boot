package com.smart.kettle.core.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

/**
 * 数据库meta配置
 * @author ShiZhongMing
 * 2021/7/15 10:26
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseMetaProperties {

    @NonNull
    private String type = "mysql";
    @NonNull
    private String access = "jdbc";

    private String name;
    @NonNull
    private String host = "localhost";
    @NonNull
    private String db;
    @NonNull
    private String port = "3306";
    @NonNull
    private String dbUser = "root";
    @NonNull
    private String dbPassword = "";
}
