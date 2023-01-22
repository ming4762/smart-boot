package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* sys_category - 分类字段
* @author SmartCodeGenerator
* 2023-1-21 21:32:15
*/
@Getter
@Setter
@TableName("sys_category")
public class SysCategoryPO extends BaseModelUserTime {

    private Long id;

    private Long parentId;

    /**
    * category_code - 分类编码
    */
    private String categoryCode;

    /**
    * category_name - 分类名称
    */
    private String categoryName;

    /**
    * seq - seq
    */
    private Integer seq;

    private String remark;

    private Boolean hasChild;

}