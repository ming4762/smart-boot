package com.smart.message.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import lombok.Getter;
import lombok.Setter;

/**
* smart_message_template - 消息模板表
* @author SmartCodeGenerator
* 2023年6月28日 下午2:06:28
*/
@Getter
@Setter
@TableName("smart_message_template")
public class SmartMessageTemplatePO extends BaseModelUserTime {

    /**
    * id - id
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * template_code - 模板编码
    */
    private String templateCode;

    /**
    * template_name - 模板名称
    */
    private String templateName;

    /**
    * template_content - 模板内容
    */
    private String templateContent;

    /**
    * delete_yn - deleteYn
    */
    private Boolean deleteYn;

    /**
    * use_yn - useYn
    */
    private Boolean useYn;

}