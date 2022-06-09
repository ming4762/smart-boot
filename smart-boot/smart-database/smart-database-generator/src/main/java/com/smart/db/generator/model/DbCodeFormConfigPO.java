package com.smart.db.generator.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author ShiZhongMing
 * 2021/5/6 15:52
 * @since 1.0
 */
@TableName("db_code_form_config")
@Getter
@Setter
public class DbCodeFormConfigPO extends DbCodeFormConfigCommonPO {
    @Serial
    private static final long serialVersionUID = 1058703315631404522L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;


}
