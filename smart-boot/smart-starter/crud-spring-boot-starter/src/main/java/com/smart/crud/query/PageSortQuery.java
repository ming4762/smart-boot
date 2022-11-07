package com.smart.crud.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页排序查询
 * @author shizhongming
 * 2021/4/24 6:47 下午
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Schema(title = "分页排序查询参数")
public class PageSortQuery extends CommonQuery {

    private static final long serialVersionUID = 401040997642894963L;


    /**
     * 排序方向已逗号分隔
     */
    @Schema(title = "排序方向，以逗号分隔", example = "desc,asc")
    private String sortOrder;

    /**
     * 排序字段 已逗号分隔
     */
    @Schema(title = "排序字段，以逗号分隔")
    private String sortName;

    /**
     * 每页条数
     */
    @Schema(title = "每页记录数")
    private Integer limit;

    /**
     * 起始记录数
     */
    @Schema(title = "分页开始记录数，page优先")
    private Integer offset = 0;

    /**
     * 页数
     */
    @Schema(title = "当前页数，page优先")
    private Integer page;

    /**
     * 关键字查询
     */
    @Schema(title = "关键字查询")
    private String keyword;
}
