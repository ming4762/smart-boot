package com.smart.sms.core.parameter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 短信发送参数
 * @author zhongming4762
 * 2023/5/25
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsSendParameter implements Serializable {

    /**
     * 发送的手机号码列表
     */
    @NotEmpty(message = "手机号码不能为空")
    private List<String> phoneNumberList;

    /**
     * 短信签名
     */
    @NotBlank(message = "短信签名不能为空")
    private String signName;

    /**
     * 短信模板
     */
    @NotBlank(message = "短信模板不能为空")
    private String template;

    /**
     * 模板参数
     */
    private String templateParameter;
}
