package com.smart.module.api.message.parameter;

import com.smart.module.api.message.constants.MessagePriorityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
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

    @Serial
    private static final long serialVersionUID = 488223451300400617L;

    /**
     * 标记发送消息的ID
     * 如果是系统创建的消息，则执行更新，否则是执行插入
     */
    private Long messageId;

    @Schema(description = "消息通道列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> messageChannelCodeList;

    @Schema(description = "接收人ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private Set<Long> toUserIds;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "内容，如果是模板消息，该字段不需要设置，其他需要设置")
    private String content;

    @Schema(description = "模板编码，模板消息必须指定")
    private String templateCode;

    @Schema(description = "模板数据")
    private transient Object templateData;

    @Schema(description = "是否是markdown消息")
    private Boolean isMarkdown;

    @Schema(description = "优先级")
    private MessagePriorityEnum priority;

    @Schema(description = "业务参数")
    private BusinessParameter business;

    @Schema(description = "短息发送参数")
    private SmsSendParameter smsSendParameter;

    @Schema(description = "邮件发送参数")
    private EmailSendParameter emailSendParameter;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SmsSendParameter implements Serializable {
        @Serial
        private static final long serialVersionUID = 6229881767018505658L;

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
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BusinessParameter implements Serializable {
        @Serial
        private static final long serialVersionUID = 1992225195508397364L;
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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmailSendParameter implements Serializable {

        @Serial
        private static final long serialVersionUID = 318050930526481815L;

        /**
         * 发件人
         */
        @NotNull
        private String from;
        /**
         * 收件人列表
         */
        private List<String> toList;
        /**
         * 抄送列表
         */
        private List<String> ccList;
    }
}
