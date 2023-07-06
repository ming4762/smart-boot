package com.smart.message.pojo.paramteter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* smart_message_template - 消息模板表
* @author SmartCodeGenerator
* 2023年6月28日 下午2:06:28
*/
@Getter
@Setter
@ToString
@Schema(description = "消息模板保存参数")
public class SmartMessageTemplateSaveUpdateParameter implements Serializable {


    /**
    * 
    */
    private Long id;
    /**
    * 模板编码
    */
    @NotNull(message = "模板编码不能为空")
    private String templateCode;
    /**
    * 模板名称
    */
    @NotNull(message = "模板名称不能为空")
    private String templateName;
    /**
    * 模板内容
    */
    @NotNull(message = "模板内容不能为空")
    private String templateContent;
    /**
    * 
    */
    private Boolean useYn;

}