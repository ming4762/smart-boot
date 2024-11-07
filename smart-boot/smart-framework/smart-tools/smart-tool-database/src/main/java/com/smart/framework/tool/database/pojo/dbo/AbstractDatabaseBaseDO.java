package com.smart.framework.tool.database.pojo.dbo;

import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 数据库查询基础类
 * @author ShiZhongMing
 * 2020/7/25 16:30
 * @since 1.0
 */
@EqualsAndHashCode
public abstract class AbstractDatabaseBaseDO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1029967642599823336L;
}
