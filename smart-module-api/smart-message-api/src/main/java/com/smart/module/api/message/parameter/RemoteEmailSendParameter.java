package com.smart.module.api.message.parameter;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 邮件发送参数
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/3 14:08
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoteEmailSendParameter {

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

    /**
     * 附件列表
     */
    private List<MultipartFile> attachmentList;
}
