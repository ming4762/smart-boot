package com.smart.db.generator.pojo.dto;

import com.smart.db.generator.constants.ButtonListEnum;
import com.smart.db.generator.model.DbCodeFormConfigPO;
import com.smart.db.generator.model.DbCodeMainPO;
import com.smart.db.generator.model.DbCodeRuleConfigPO;
import com.smart.db.generator.model.DbCodeSearchConfigPO;
import com.smart.db.generator.pojo.vo.DbCodePageConfigTemplateVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 单一表格主数据
 * @author ShiZhongMing
 * 2021/5/8 10:08
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class DbTemplateCodeTableDTO extends DbCodeMainPO {
    @Serial
    private static final long serialVersionUID = 2839798474432673236L;

    public DbTemplateCodeTableDTO() {
        this.hasId = Boolean.FALSE;
        this.modelClassImportList = new HashSet<>(0);
        this.leftButtonList = new ArrayList<>(0);
        this.rightButtonList = new ArrayList<>(0);
        this.rowButtonList = new ArrayList<>(0);
    }

    /**
     * 是否有ID
     */
    private Boolean hasId;

    /**
     * ID字段信息
     */
    private DbCodePageConfigTemplateVO idField;

    /**
     * 关联字段（用户select-table等）
     */
    private String relatedColumn;

    /**
     * 实体类引入列表
     */
    private Set<String> modelClassImportList;

    /**
     * 页面配置
     */
    private List<DbCodePageConfigTemplateVO> codePageConfigList;

    /**
     * 表单配置
     */
    private List<DbCodeFormConfigTemplateDTO> codeFormConfigList;

    /**
     * 搜索配置
     */
    private List<DbCodeSearchConfigTemplateDTO> codeSearchConfigList;

    /**
     * 按钮列表
     */
    private List<ButtonListEnum> leftButtonList;

    private List<ButtonListEnum> rightButtonList;

    private List<ButtonListEnum> rowButtonList;



    @Getter
    @Setter
    @ToString
    public static class DbCodeFormConfigTemplateDTO extends DbCodeFormConfigPO {

        private static final long serialVersionUID = -4075245994474313790L;

        /**
         * 下拉表格
         */
        private DbTemplateCodeTableDTO selectTable;

        /**
         * 验证规则
         */
        private List<DbCodeRuleConfigPO> ruleList;
    }

    @Getter
    @Setter
    @ToString
    public static class DbCodeSearchConfigTemplateDTO extends DbCodeSearchConfigPO {

        private static final long serialVersionUID = -4075245994474313790L;

        /**
         * 下拉表格
         */
        private DbTemplateCodeTableDTO selectTable;
    }
}
