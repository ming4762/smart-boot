package com.smart.framework.crud.query;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * ID分页排序查询
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-20 01:23
 * @since 5.0.0
 */
@Getter
@Setter
public class IdPageSortQuery extends PageSortQuery {

    @NotNull(message = "ID不能为空")
    private Long id;
}
