package com.smart.framework.crud.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 分页排序查询
 * @author shizhongming
 * 2021/4/24 6:47 下午
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页排序查询参数")
public class PageSortQuery extends SortQuery {

    @Serial
    private static final long serialVersionUID = 401040997642894963L;

    /**
     * 每页条数
     */
    @Schema(description = "每页记录数，优先级低")
    private Integer pageSize;


    @Schema(description = "当前页数，page优先")
    private Integer currentPage;

}
