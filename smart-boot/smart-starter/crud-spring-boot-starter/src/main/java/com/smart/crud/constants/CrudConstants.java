package com.smart.crud.constants;

import com.baomidou.mybatisplus.core.enums.SqlMethod;

import java.util.List;

/**
 * CRUD模块常量
 * @author shizhongming
 * 2024/4/20 20:32
 * @since 3.0.0
 */
public interface CrudConstants {

    /**
     * 支持逻辑删除的函数
     */
    List<String> LOGIC_DELETE_METHODS = List.of(
            SqlMethod.DELETE_BY_ID.getMethod(),
            SqlMethod.DELETE_BATCH_BY_IDS.getMethod(),
            SqlMethod.DELETE.getMethod()
    );
}
