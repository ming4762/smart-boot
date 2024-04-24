package com.smart.kettle.core.properties;

import com.smart.kettle.core.constants.DatabaseAccessEnum;
import com.smart.kettle.core.constants.DatabaseTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder(toBuilder = true)
public class DatabaseMetaProperties {

    /**
     * 数据库类型
     */
    @NonNull
    private DatabaseTypeEnum type;
    /**
     * 连接方式
     */
    @NonNull
    private DatabaseAccessEnum access;

    private String name;
    @NonNull
    private String host;
    @NonNull
    private String db;
    @NonNull
    private String port;
    @NonNull
    private String dbUser;
    @NonNull
    private String dbPassword;
}
