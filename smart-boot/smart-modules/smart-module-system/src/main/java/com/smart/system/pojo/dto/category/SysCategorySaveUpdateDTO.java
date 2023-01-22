package com.smart.system.pojo.dto.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

}