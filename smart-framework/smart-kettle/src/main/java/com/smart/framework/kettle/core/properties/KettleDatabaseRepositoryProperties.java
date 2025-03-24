package com.smart.framework.kettle.core.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * kettle 数据库资源库配置参数
 * @author ShiZhongMing
 * 2021/7/15 10:32
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class KettleDatabaseRepositoryProperties extends DatabaseMetaProperties {

    @Serial
    private static final long serialVersionUID = -6533489911003676893L;

    private String resUser;

    private String resPassword;

    private String id;

    private String repositoryName;

    private String description;
}
