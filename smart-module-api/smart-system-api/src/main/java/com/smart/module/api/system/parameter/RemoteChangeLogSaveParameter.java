package com.smart.module.api.system.parameter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 变更记录保存参数
 * @author zhongming4762
 * 2023/8/1 9:25
 */
@Getter
@Setter
@Schema(description = "变更记录保存参数")
public class RemoteChangeLogSaveParameter extends CommonChangeLogSaveParameter implements Serializable {

    @Schema(description = "详细信息")
    private List<Detail> detailList;

    @Getter
    @AllArgsConstructor
    @Schema(description = "变更记录详细信息")
    public static class Detail implements Serializable {
        private final String changeField;

        private final String beforeValue;

        private final String afterValue;
    }
}
