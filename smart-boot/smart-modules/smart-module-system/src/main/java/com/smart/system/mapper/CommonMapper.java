package com.smart.system.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * @author zhongming4762
 * 2023/2/14 18:17
 */
public interface CommonMapper {

    /**
     * 更新是否有下级
     * @param tableName 更新的表名
     * @param parentField parent field
     * @param idField id field
     * @param id ID
     * @return 更新记录数
     */
    Integer updateHasChild(
            @Param("tableName") String tableName,
            @Param("parentField") String parentField,
            @Param("idField") String idField,
            @Param("id")Serializable id
            );
}
