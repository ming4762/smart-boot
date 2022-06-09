package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * 搜索配置类
 * @author shizhongming
 * 2021/5/9 8:00 下午
 */
@TableName("db_code_search_config")
@Getter
@Setter
public class DbCodeSearchConfigPO extends DbCodeFormConfigCommonPO {
    @Serial
    private static final long serialVersionUID = -8137689298989008494L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 搜索符号
     */
    private String searchSymbol;

}
