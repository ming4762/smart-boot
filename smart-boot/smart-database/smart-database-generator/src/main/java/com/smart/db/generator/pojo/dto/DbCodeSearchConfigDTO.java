package com.smart.db.generator.pojo.dto;

import com.smart.db.generator.model.DbCodeRelatedTablePO;
import com.smart.db.generator.model.DbCodeSearchConfigPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 搜索配置信息DTO
 * @author ShiZhongMing
 * 2021/5/13 9:02
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCodeSearchConfigDTO extends DbCodeSearchConfigPO {
    private static final long serialVersionUID = -8062136073063162454L;

    /**
     * 下拉表格配置信息
     */
    private List<DbCodeRelatedTablePO> selectTableList;
}
