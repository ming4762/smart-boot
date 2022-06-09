package com.smart.module.document.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.crud.model.BaseModelUserTime;
import com.smart.module.document.constants.TemplateIdentEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2021/8/14 8:04 下午
 */
@Getter
@Setter
@TableName("document_template")
public class DocumentTemplatePO extends BaseModelUserTime {
    @Serial
    private static final long serialVersionUID = 6108967120733387606L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long templateId;

    private String templateCode;

    private String templateName;

    private byte[] data;

    private String remark;

    private Integer seq;

    private TemplateIdentEnum ident;
}
