package com.smart.module.api.crud.module;

import com.smart.module.api.crud.constants.DataPermissionScopeEnum;
import lombok.*;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;

/**
 * 数据权限模型
 * @author shizhongming
 * 2025/3/5 20:20
 * @since 5.0.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmartDataPermissionModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 7587823326556024808L;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限字段
     */
    private String column;

    /**
     * 权限范围
     */
    private DataPermissionScopeEnum scope;

    /**
     * 自定义数据权限规则值
     */
    private String permissionValue;

    /**
     * 表名
     */
    @Nullable
    private String tableName;

    private Class<?> tableClass;

    private String mapperStatementId;
}
