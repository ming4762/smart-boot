package com.smart.db.generator.pojo.vo;

import com.smart.db.generator.constants.ButtonListEnum;
import com.smart.db.generator.model.DbCodeMainPO;
import com.smart.db.generator.model.DbCodePageConfigPO;
import com.smart.db.generator.pojo.dto.DbCodeFormConfigDTO;
import com.smart.db.generator.pojo.dto.DbCodeSearchConfigDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class DbMainConfigVO extends DbCodeMainPO {
    private static final long serialVersionUID = -8319201416653035913L;

    /**
     * 页面配置
     */
    private List<DbCodePageConfigPO> codePageConfigList;

    /**
     * 表单配置
     */
    private List<DbCodeFormConfigDTO> codeFormConfigList;

    /**
     * 搜索配置
     */
    private List<DbCodeSearchConfigDTO> codeSearchConfigList;

    /**
     * 附表信息
     */
    private List<DbCodeRelatedTableVO> relatedTableList;

    /**
     * 按钮列表
     */
    private List<ButtonListEnum> leftButtonList;

    private List<ButtonListEnum> rightButtonList;

    private List<ButtonListEnum> rowButtonList;

    /**
     * 附表信息
     */
    @Getter
    @Setter
    @ToString
    @Builder
    public static class DbCodeRelatedTableVO implements Serializable {
        private static final long serialVersionUID = 3342580057520197926L;
        private Long mainId;

        /**
         * 附表ID
         */
        private Long addendumId;

        /**
         * 配置名称
         */
        private String configName;

        private String relatedColumn;
    }
}
