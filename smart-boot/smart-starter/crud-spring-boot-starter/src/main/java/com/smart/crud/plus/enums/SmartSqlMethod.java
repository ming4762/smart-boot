package com.smart.crud.plus.enums;

import lombok.Getter;

/**
 * MybatisPlus 支持 SQL 方法
 * 添加逻辑删除指定逻辑删除key
 * @author shizhongming
 * 2023/10/31 13:53
 * @since 3.0.0
 */
@Getter
public enum SmartSqlMethod {

    LOGIC_DELETE_BY_ID("deleteById", "根据ID 逻辑删除一条数据", "<script>\nUPDATE %s %s WHERE %s=#{%s} %s\n</script>"),
    LOGIC_DELETE_BY_MAP("deleteByMap", "根据columnMap 条件逻辑删除记录", "<script>\nUPDATE %s %s, %s %s\n</script>"),
    LOGIC_DELETE("delete", "根据 entity 条件逻辑删除记录", "<script>\nUPDATE %s %s, %s %s %s\n</script>"),
    LOGIC_DELETE_BATCH_BY_IDS_WITH_KEY("deleteBatchIds", "根据ID集合，批量逻辑删除数据", "<script>\nUPDATE %s %s WHERE %s IN (%s) %s\n</script>"),

    ;
    private final String method;
    private final String desc;
    private final String sql;

    SmartSqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }
}
