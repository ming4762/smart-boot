package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.db.analysis.constants.DatabaseTypeEnum;
import com.smart.db.analysis.pool.model.DbConnectionConfig;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/4/25 16:29
 * @since 1.0
 */
@Getter
@Setter
@TableName("db_connection")
public class DbConnectionPO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = -4806669717234559319L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @NotNull
    private String project;

    private String connectionName;

    private String databaseName;

    @TableField("database_type")
    private DatabaseTypeEnum type;

    private String url;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String tableSchema;

    private Integer seq;

    private Boolean useYn;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Boolean deleteYn;

    /**
     * 创建数据库连接配置
     * @return DbConnectionConfig
     */
    public DbConnectionConfig createConnectionConfig() {
        return new DbConnectionConfig(this.databaseName, this.type, this.url, this.username, this.password, this.tableSchema);
    }
}
