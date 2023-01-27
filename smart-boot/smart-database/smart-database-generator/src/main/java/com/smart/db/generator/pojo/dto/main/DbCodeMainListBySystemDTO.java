package com.smart.db.generator.pojo.dto.main;

import com.smart.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhongming4762
 * 2023/1/27 16:30
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DbCodeMainListBySystemDTO extends PageSortQuery {

    private Long systemId;
}
