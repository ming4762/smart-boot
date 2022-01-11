package com.smart.kettle.core.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class KettleDatabaseRepositoryProperties extends DatabaseMetaProperties{

    private String resUser = "admin";

    private String resPassword = "admin";

    private String id;

    private String repositoryName;

    private String description;

    public KettleDatabaseRepositoryProperties(String type, String access, String name, String host, String db, String port, String dbUser, String dbPassword,
                                              String resUser, String resPassword, String id, String repositoryName, String description) {
        super(type, access, name, host, db, port, dbUser, dbPassword);
        this.resUser = resUser;
        this.resPassword = resPassword;
        this.id = id;
        this.repositoryName = repositoryName;
        this.description = description;
    }
}
