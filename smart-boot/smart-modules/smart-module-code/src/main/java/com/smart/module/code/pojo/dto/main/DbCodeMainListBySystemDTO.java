package com.smart.module.code.pojo.dto.main;

import com.smart.framework.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author zhongming4762
 * 2023/1/27 16:30
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DbCodeMainListBySystemDTO extends PageSortQuery {

    @Serial
    private static final long serialVersionUID = 4813613290965158530L;

    private Long systemId;
}
