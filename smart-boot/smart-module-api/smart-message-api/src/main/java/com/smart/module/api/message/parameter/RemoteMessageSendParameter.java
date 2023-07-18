package com.smart.module.api.message.parameter;

import com.smart.module.api.message.constants.MessageChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
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
public class RemoteMessageSendParameter implements Serializable {

    @Schema(description = "接收人ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Long> toUserIds;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "内容，如果是模板消息，该字段不需要设置，其他需要设置")
    private String content;

    @Schema(description = "模板编码，模板消息必须指定")
    private String templateCode;

    @Schema(description = "模板数据")
    private LinkedHashMap<String, String> templateData;

    /**
     * 消息通道
     */
    @Schema(description = "消息通道列表")
    private Set<MessageChannelEnum> messageChannels;

    @Schema(description = "业务参数")
    private BusinessParameter business;

    @Schema(description = "短息发送参数")
    private SmsSendParameter smsSendParameter;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SmsSendParameter implements Serializable {
        /**
         * 发送的手机号码列表
         */
        @Schema(description = "手机号码列表", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "手机号码不能为空")
        private List<String> phoneNumberList;

        /**
         * 短信签名
         */
        @Schema(description = "短信签名", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "短信签名不能为空")
        private String signName;

        /**
         * 短信模板
         */
        @Schema(description = "短信模板", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "短信模板不能为空")
        private String template;

        @Schema(description = "短信通道编码")
        private String channelCode;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class BusinessParameter implements Serializable {
        /**
         * business_ident - 业务标识位
         */
        private String businessIdent;

        /**
         * business_id - 业务ID
         */
        private Long businessId;

        /**
         * business_data - 业务数据
         */
        private String businessData;
    }
}
