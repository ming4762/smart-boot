package com.smart.module.code.pojo.dto;

import com.smart.framework.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author zhongming4762
 * 2023/1/24 18:23
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DbConnectionListBySystemDTO extends PageSortQuery {

    @Serial
    private static final long serialVersionUID = -1368979195206527366L;

    private Long systemId;
}
