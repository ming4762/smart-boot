package com.smart.db.generator.pojo.dto;

import com.smart.db.generator.constants.ButtonListEnum;
import com.smart.db.generator.constants.RuleTriggerEnum;
import com.smart.db.generator.constants.RuleTypeEnum;
import com.smart.db.generator.model.DbCodeFormConfigPO;
import com.smart.db.generator.model.DbCodeMainPO;
import com.smart.db.generator.model.DbCodePageConfigPO;
import com.smart.db.generator.model.DbCodeSearchConfigPO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/5/6 15:50
 * @since 1.0
 */
@ToString
@Getter
@Setter
public class DbCodeMainSaveParameter extends DbCodeMainPO {
    @Serial
    private static final long serialVersionUID = -2821275792943359287L;

    /**
     * 附表集合
     */
    private List<DbCodeRelatedTableParameter> addendumTableList;

    /**
     * 按钮列表
     */
    private List<ButtonListEnum> leftButtonList;

    private List<ButtonListEnum> rightButtonList;

    private List<ButtonListEnum> rowButtonList;

    /**
     * 页面配置
     */
    private List<DbCodePageConfigPO> codePageConfigList;

    /**
     * 表单配置
     */
    private List<DbCodeFormConfigParameter> codeFormConfigList;

    /**
     * 搜索配置
     */
    private List<DbCodeSearchConfigParameter> codeSearchConfigList;

    /**
     * 搜索配置信息
     */
    @Getter
    @Setter
    @ToString
    public static class DbCodeSearchConfigParameter extends DbCodeSearchConfigPO {

        @Serial
        private static final long serialVersionUID = -7138318701088963477L;

        /**
         * 下拉表格信息
         */
        private List<DbCodeRelatedTableParameter> selectTableList;
    }

    /**
     * form配置信息
     */
    @Getter
    @Setter
    @ToString
    public static class DbCodeFormConfigParameter extends DbCodeFormConfigPO {

        @Serial
        private static final long serialVersionUID = 4911110887343927163L;

        /**
         * 下拉表格信息
         */
        private List<DbCodeRelatedTableParameter> selectTableList;

        /**
         * 校验规则
         */
        private List<DbCodeRuleSaveDTO> ruleList;
    }

    /**
     * 关联表信息
     */
    @Getter
    @Setter
    @ToString
    public static class DbCodeRelatedTableParameter implements Serializable {
        @Serial
        private static final long serialVersionUID = 7848554623697145564L;
        /**
         * 附表ID
         */
        private Long addendumId;

        /**
         * 关联字段
         */
        private String relatedColumn;
    }

    /**
     * 校验信息保存DTO
     */
    @Getter
    @Setter
    @ToString
    public static class DbCodeRuleSaveDTO implements Serializable {
        @Serial
        private static final long serialVersionUID = -5973595319886211762L;
        @NotNull(message = "验证类型不能为空")
        private RuleTypeEnum ruleType;

        @NotEmpty(message = "验证实际不能为空")
        private List<RuleTriggerEnum> ruleTrigger;

        private Long len;

        private Long max;

        private Long min;

        @NotNull(message = "校验文案不能为空")
        private String message;

        private String pattern;

    }
}
