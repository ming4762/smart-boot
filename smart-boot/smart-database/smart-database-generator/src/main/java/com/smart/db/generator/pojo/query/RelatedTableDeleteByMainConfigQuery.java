package com.smart.db.generator.pojo.query;

import lombok.*;

import java.util.Collection;

/**
 * 通过主表删除表关联信息
 * @author ShiZhongMing
 * 2021/5/13 11:28
 * @since 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatedTableDeleteByMainConfigQuery {

    private Collection<?> mainIdList;

    private String tableName;
}
