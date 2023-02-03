package com.smart.system.pojo.dto.user;

import com.smart.crud.query.PageSortQuery;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    private List<Long> deptIdList;
}
