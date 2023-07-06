package com.smart.module.api.message.parameter;

import com.smart.module.api.message.constants.MessageChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 发送消息参数
 * @author zhongming4762
 * 2023/6/27
 */
@Getter
@Setter
@ToString
@Schema(description = "消息发送参数")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSendParameter implements Serializable {

    @Schema(description = "接收人ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Long> toUserIds;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "内容，如果是模板消息，该字段不需要设置，其他需要设置")
    private String content;

    @Schema(description = "模板编码，模板消息必须指定")
    private String templateCode;

    @Schema(description = "模板数据")
    private Map<String, String> templateData;

    /**
     * 消息通道
     */
    @Schema(description = "消息通道列表")
    private Set<MessageChannelEnum> messageChannels;

}
