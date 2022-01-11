package com.smart.db.generator.pojo.vo;

import com.smart.db.generator.pojo.dto.DbTemplateCodeTableDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成数据
 * @author ShiZhongMing
 * 2021/5/8 10:31
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbCodeTemplateDataVO implements Serializable {

    private static final long serialVersionUID = -5976055009644318861L;

    private String className;

    private String description;

    private String packages;

    /**
     * ext包名
     */
    private String extPackages;

    /**
     * controller基础路径
     */
    private String controllerBasePath;

    /**
     * 主表数据
     */
    private DbTemplateCodeTableDTO mainTable;

    /**
     * 附表数据
     */
    private List<DbTemplateCodeTableDTO> addendumTableList;
}
