package com.smart.framework.crud.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 排序查询
 * @author shizhongming
 * 2025/9/12 17:03
 * @since 5.0.0
 */
@Getter
@Setter
public class SortQuery extends CommonQuery {

    /**
     * 排序方向已逗号分隔
     */
    @Schema(description = "排序方向，以逗号分隔", example = "desc,asc")
    private String sortOrder;

    /**
     * 排序字段 已逗号分隔
     */
    @Schema(description = "排序字段，以逗号分隔")
    private String sortName;
}
