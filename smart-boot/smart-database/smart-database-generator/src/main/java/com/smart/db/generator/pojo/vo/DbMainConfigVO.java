package com.smart.db.generator.pojo.vo;

import com.smart.db.generator.constants.ButtonListEnum;
import com.smart.db.generator.model.DbCodeMainPO;
import com.smart.db.generator.model.DbCodePageConfigPO;
import com.smart.db.generator.pojo.dto.DbCodeFormConfigDTO;
import com.smart.db.generator.pojo.dto.DbCodeSearchConfigDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 主配置VO类
 * @author ShiZhongMing
 * 2021/5/10 14:13
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(description = "代码配置")
public class DbMainConfigVO extends DbCodeMainPO {
    @Serial
    private static final long serialVersionUID = -8319201416653035913L;

    /**
     * 页面配置
     */
    @Schema(description = "表格配置")
    private List<DbCodePageConfigPO> codePageConfigList;

    /**
     * 表单配置
     */
    @Schema(description = "表单配置")
    private List<DbCodeFormConfigDTO> codeFormConfigList;

    /**
     * 搜索配置
     */
    @Schema(description = "搜索表单配置")
    private List<DbCodeSearchConfigDTO> codeSearchConfigList;

    /**
     * 附表信息
     */
    @Schema(description = "附表列表")
    private List<DbCodeRelatedTableVO> relatedTableList;

    /**
     * 按钮列表
     */
    @Schema(description = "左侧按钮列表")
    private List<ButtonListEnum> leftButtonList;

    @Schema(description = "右侧按钮列表")
    private List<ButtonListEnum> rightButtonList;

    @Schema(description = "行按钮列表")
    private List<ButtonListEnum> rowButtonList;

    /**
     * 附表信息
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @Schema(description = "附表信息")
    public static class DbCodeRelatedTableVO implements Serializable {
        private static final long serialVersionUID = 3342580057520197926L;

        @Schema(description = "主表ID")
        private Long mainId;

        /**
         * 附表ID
         */
        @Schema(description = "附表ID")
        private Long addendumId;

        /**
         * 配置名称
         */
        @Schema(description = "配置名称")
        private String configName;

        @Schema(description = "关联列")
        private String relatedColumn;
    }
}
