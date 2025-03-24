package com.smart.framework.crud.plus.metadata;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import lombok.*;
import org.apache.ibatis.mapping.SqlCommandType;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 租户字段信息
 * @author shizhongming
 * 2024/4/11 16:14
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableTenantFieldInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 5941645837247140924L;

    private TableFieldInfo tableFieldInfo;

    /**
     * 忽略SQL命令
     */
    private List<SqlCommandType> ignoreCommandList;

    /**
     * 平台管理租户忽略的命令
     */
    private List<SqlCommandType> platformTenantIgnoreCommandList;

    /**
     * 是否租户默认字段
     * 如果存在多个租户字段，必须存在默认租户字段
     */
    private boolean defaultField;
}
