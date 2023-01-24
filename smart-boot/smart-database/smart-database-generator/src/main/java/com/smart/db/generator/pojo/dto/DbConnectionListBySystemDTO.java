package com.smart.db.generator.pojo.dto;

import com.smart.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/1/24 18:23
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DbConnectionListBySystemDTO extends PageSortQuery {

    private Long systemId;
}
