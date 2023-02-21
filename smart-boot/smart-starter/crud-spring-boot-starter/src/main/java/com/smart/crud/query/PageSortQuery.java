package com.smart.crud.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.Optional;

/**
 * 分页排序查询
 * @author shizhongming
 * 2021/4/24 6:47 下午
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页排序查询参数")
public class PageSortQuery extends CommonQuery {

    @Serial
    private static final long serialVersionUID = 401040997642894963L;


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

    /**
     * 每页条数
     */
    @Schema(description = "每页记录数，优先级高")
    private Integer limit;

    /**
     * 每页条数
     */
    @Schema(description = "每页记录数，优先级低")
    private Integer pageSize;

    /**
     * 起始记录数
     */
    @Schema(description = "分页开始记录数，page优先")
    private Integer offset = 0;

    /**
     * 页数
     */
    @Schema(description = "当前页数，page优先")
    private Integer page;

    @Schema(description = "当前页数，page优先")
    private Integer currentPage;

    /**
     * 关键字查询
     */
    @Schema(description = "查询关键字")
    private String keyword;

    public Integer getLimit() {
        return Optional.ofNullable(this.limit)
                .orElse(this.pageSize);
    }

    public Integer getPage() {
        return Optional.ofNullable(this.page)
                .orElse(this.currentPage);
    }
}
