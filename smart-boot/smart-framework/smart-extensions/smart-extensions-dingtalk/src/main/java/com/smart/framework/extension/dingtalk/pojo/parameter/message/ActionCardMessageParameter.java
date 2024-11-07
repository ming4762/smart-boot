package com.smart.framework.extension.dingtalk.pojo.parameter.message;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 卡片消息
 * @author shizhongming
 * 2024/4/28 20:33
 * @since 3.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionCardMessageParameter extends AbstractMessageParameter {

    @Serial
    private static final long serialVersionUID = -3698674571972099730L;

    @NotNull(message = "内容不能为空")
    private String markdown;

    /**
     * 透出到会话列表和通知的文案。
     */
    private String title;

    /**
     * 如果是整体跳转的ActionCard样式，则single_title和single_url必须设置。
     */
    private String singleTitle;

    /**
     * 消息点击链接地址，当发送消息为小程序时支持小程序跳转链接，最长500个字符
     */
    private String singleUrl;

    /**
     * 使用独立跳转ActionCard样式时的按钮排列方式
     * 0：竖直排列
     * 1：横向排列
     * 必须与btn_json_list同时设置
     */
    private String btnOrientation;

    private List<Button> buttonList;


    public record Button(String title, String actionUrl) implements Serializable {

    }
}
