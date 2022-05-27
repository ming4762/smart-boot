package com.smart.system.pojo.dto.dict;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
* sys_dict_item - 字典序表
* @author GCCodeGenerator
* 2022-2-7 10:48:32
*/
@Getter
@Setter
@ToString
public class SysDictItemSaveUpdateDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -1933118566698565044L;
    /**
    * 字典编码
    */
    private String dictCode;
    /**
    * 字典项编码
    */
    @NotNull(message = "字典项编码不能为空")
    private String dictItemCode;
    /**
    * 字典项名称
    */
    @NotNull(message = "字典项名称不能为空")
    private String dictItemName;
    /**
    * 序号
    */
    @NotNull(message = "序号不能为空")
    private Integer seq;
    /**
    * 启用状态
    */
    private Boolean useYn;
    /**
    * 描述
    */
    private String remark;

}
