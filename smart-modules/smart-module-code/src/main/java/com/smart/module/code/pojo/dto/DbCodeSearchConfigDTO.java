package com.smart.module.code.pojo.dto;

import com.smart.module.code.model.DbCodeRelatedTablePO;
import com.smart.module.code.model.DbCodeSearchConfigPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
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
    @Serial
    private static final long serialVersionUID = -8062136073063162454L;

    /**
     * 下拉表格配置信息
     */
    private List<DbCodeRelatedTablePO> selectTableList;
}
