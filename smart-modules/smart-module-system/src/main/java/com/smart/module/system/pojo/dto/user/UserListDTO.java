package com.smart.module.system.pojo.dto.user;

import com.smart.framework.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * 用户查询参数
 * @author zhongming4762
 * 2023/2/3
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserListDTO extends PageSortQuery {

    @Serial
    private static final long serialVersionUID = -3179733381629820554L;

    private List<Long> deptIdList;

    private Boolean useYn;

    private Long tenantId;
}
