package com.smart.framework.kettle.core.properties;

import com.smart.framework.kettle.core.constants.DatabaseAccessEnum;
import com.smart.framework.kettle.core.constants.DatabaseTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.jspecify.annotations.NonNull;

import java.io.Serial;
import java.io.Serializable;

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
@EqualsAndHashCode
public class DatabaseMetaProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = -5741131847736470395L;
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

    /**
     * 数据库是否强制使用小写
     */
    private Boolean forceIdentifiersToLowercase;

    /**
     * 数据库是否强制使用大写
     */
    private Boolean forceIdentifiersToUppercase;
}
