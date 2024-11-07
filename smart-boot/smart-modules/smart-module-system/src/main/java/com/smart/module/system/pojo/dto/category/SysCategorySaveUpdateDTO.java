package com.smart.module.system.pojo.dto.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
* sys_category - 分类字段
* @author SmartCodeGenerator
* 2023-1-21 21:32:15
*/
@Getter
@Setter
@ToString
public class SysCategorySaveUpdateDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = 5994028748990028924L;
    /**
    * 
    */
    private Long id;
    /**
    * 
    */
    private Long parentId;
    /**
    * 分类编码
    */
    private String categoryCode;
    /**
    * 分类名称
    */
    private String categoryName;
    /**
    * 
    */
    private Integer seq;
    /**
    * 
    */
    private String remark;

    /**
     * 是否平台通用
     */
    private Boolean tenantCommonYn;

}