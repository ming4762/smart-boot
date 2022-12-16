package com.smart.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
* sys_dict_item - 字典序表
* @author GCCodeGenerator
* 2022-2-7 10:48:32
*/
@Getter
@Setter
@TableName("sys_dict_item")
public class SysDictItemPO extends BaseModel {

    @Serial
    private static final long serialVersionUID = 4637452253459448214L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * dict_code - 字典编码
    */
    private Long dictId;

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

    /**
    * delete_yn - deleteYn
    */
    private Byte deleteYn;

    /**
    * create_user_id - createUserId
    */
    private Long createUserId;

    /**
    * create_time - createTime
    */
    private LocalDateTime createTime;

    /**
    * update_user_id - updateUserId
    */
    private Long updateUserId;

    /**
    * update_time - updateTime
    */
    private LocalDateTime updateTime;

}
