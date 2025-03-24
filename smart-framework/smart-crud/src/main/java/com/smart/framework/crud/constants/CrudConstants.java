package com.smart.framework.crud.constants;

import com.baomidou.mybatisplus.core.enums.SqlMethod;

import java.util.List;

/**
 * CRUD模块常量
 * @author shizhongming
 * 2024/4/20 20:32
 * @since 3.0.0
 */
public final class CrudConstants {

    /**
     * 支持逻辑删除的函数
     */
    public static final List<String> LOGIC_DELETE_METHODS = List.of(
            SqlMethod.LOGIC_DELETE_BY_ID.getMethod(),
            SqlMethod.LOGIC_DELETE_BY_MAP.getMethod(),
            SqlMethod.LOGIC_DELETE.getMethod(),
            SqlMethod.LOGIC_DELETE_BY_IDS.getMethod()
    );
}
