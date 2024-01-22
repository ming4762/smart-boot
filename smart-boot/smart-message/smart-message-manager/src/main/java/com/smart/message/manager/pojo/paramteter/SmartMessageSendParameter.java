package com.smart.message.manager.pojo.paramteter;

import com.smart.crud.query.PageSortQuery;
import com.smart.message.manager.constants.MessageTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author shizhongming
 * 2023/1/20 14:35
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class SmartMessageSendParameter extends PageSortQuery {

    @Serial
    private static final long serialVersionUID = -2383879068264187235L;
    @Schema(description = "标题")
    private String title;

    /**
     * 消息类型
     */
    @Schema(description = "消息类型")
    private MessageTypeEnum messageType;
}
