package com.smart.module.api.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询字典返回结果
 * @author zhongming4762
 * 2023/6/1
 */
@Getter
@Setter
@ToString
public class SysDictItemDTO implements Serializable {

    private Long id;

    /**
     * dict_code - 字典编码
     */
    private Long dictId;

    private String dictCode;

    /**
     * dict_item_code - 字典项编码
     */
    private String dictItemCode;

    /**
     * dict_item_name - 字典项名称
     */
    private String dictItemName;

    /**
     * seq - 序号
     */
    private Integer seq;

    /**
     * remark - 描述
     */
    private String remark;

    /**
     * use_yn - 启用状态
     */
    private Boolean useYn;
}
