package com.smart.db.generator.pojo.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ShiZhongMing
 * 2021/5/8 9:50
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Builder
public class DbCodeVO implements Serializable {
    private static final long serialVersionUID = -5766317828450057376L;

    private Long templateId;

    private String templateName;

    private String language;

    private String code;

    private String filename;
}
