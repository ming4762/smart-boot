package com.smart.message.manager.pojo.paramteter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 发送测试参数
 * @author zhongming4762
 * 2023/6/5
 */
@Getter
@Setter
public class SmartSmsSendTestParameter implements Serializable {

    private Long channelId;

    @NotEmpty(message = "手机号码不能为空")
    private List<String> phoneNumberList;

    @NotBlank(message = "短信签名不能为空")
    private String signName;

    @NotBlank(message = "短信模板不能为空")
    private String template;

    private LinkedHashMap<String, String> templateParameter;
}
