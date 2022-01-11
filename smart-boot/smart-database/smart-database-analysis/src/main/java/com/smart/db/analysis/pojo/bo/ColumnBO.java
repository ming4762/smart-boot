package com.smart.db.analysis.pojo.bo;

import com.smart.commons.core.utils.StringUtils;
import com.smart.db.analysis.pojo.dbo.ColumnDO;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 列业务类
 * @author shizhongming
 * 2020/1/20 8:56 下午
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ColumnBO extends ColumnDO {
    private static final long serialVersionUID = 6189047510460416526L;

    public ColumnBO() {
        this.primaryKey = Boolean.FALSE;
        this.importKey = Boolean.FALSE;
        this.indexed = Boolean.FALSE;
    }

    /**
     * 批量通过DO 创建BO
     * @author shizhongming
     * @param columnList 字段DO列表
     * @return 字段BO列表
     */
    public static List<ColumnBO> batchCreateFromDo(@NonNull List<ColumnDO> columnList) {
        return columnList.stream()
                .map(ColumnBO::createFromDo)
                .collect(Collectors.toList());
    }

    /**
     * 通过DO创建BO
     * @param column 列DO
     * @return 字段BO
     */
    public static ColumnBO createFromDo(@NonNull ColumnDO column) {
        ColumnBO columnBo = new ColumnBO();
        BeanUtils.copyProperties(column, columnBo);
        // 设置java属性名
        columnBo.javaProperty = StringUtils.lineToHump(column.getColumnName());
        return columnBo;
    }

    // --------- 主键信息 -----------
    /**
     * 是否是主键
     */
    private Boolean primaryKey;

    /**
     * 主键序号
     */
    private Integer keySeq;

    /**
     * 主键名称
     */
    private String pkName;

    // -------- 外键信息 --------
    /**
     * 是否是外键
     */
    private Boolean importKey;

    /**
     * 外键名称
     */
    private String importPkName;

    // -------- 索引信息 ---------

    /**
     * 是否是索引
     */
    private Boolean indexed;

    /**
     * 是否是唯一索引
     */
    private Boolean unique;

    /**
     * 索引类型
     */
    private Integer indexType;

    // ------- 其他信息 -----------
    /**
     * java类型
     */
    private String javaType;

    /**
     * java类型简写
     */
    private String simpleJavaType;

    /**
     * java属性名
     */
    private String javaProperty;

    /**
     * ext类型
     */
    private String extType;
}
