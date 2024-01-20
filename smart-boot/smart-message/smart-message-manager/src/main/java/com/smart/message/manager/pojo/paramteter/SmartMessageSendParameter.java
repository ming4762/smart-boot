package com.smart.message.manager.pojo.paramteter;

import com.smart.crud.query.PageSortQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shizhongming
 * 2023/1/20 14:35
 * @since 3.0.0
 */
@Getter
@Setter
public class SmartMessageSendParameter extends PageSortQuery {

    @Schema(description = "标题")
    private String title;
}
