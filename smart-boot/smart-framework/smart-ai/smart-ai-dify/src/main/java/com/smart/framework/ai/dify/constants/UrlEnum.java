package com.smart.framework.ai.dify.constants;

import lombok.Getter;
import org.springframework.http.HttpMethod;

/**
 * DIFY API URL
 * @author shizhongming
 * 2025/2/8 20:21
 * @since 5.0.0
 */
@Getter
public enum UrlEnum {

    /**
     * 发送对话消息
     */
    CHAT_MESSAGES("/chat-messages", HttpMethod.POST, "发送对话消息"),
    FILES_UPLOAD("/files/upload", HttpMethod.POST, "上传文件"),
    STOP_CHAT("/chat-messages/%s/stop", HttpMethod.POST, "停止对话"),
    FEEDBACKS_MESSAGES("/messages/%s/feedbacks", HttpMethod.POST, "反馈对话"),
    SUGGESTED_MESSAGES("/messages/%s/suggested", HttpMethod.GET,"获取下一轮建议问题列表"),
    MESSAGES_HISTORY("/messages", HttpMethod.GET,"获取对话历史"),
    CONVERSATIONS("/conversations", HttpMethod.GET,"获取对话列表"),
    DELETE_CONVERSATIONS("/conversations/%s", HttpMethod.DELETE,"删除对话"),
    RENAME_CONVERSATIONS("/conversations/%s/name", HttpMethod.POST,"重命名对话"),
    AUDIO_TO_TEXT("/audio-to-text", HttpMethod.POST,"音频转文本"),
    TEXT_TO_AUDIO("/text-to-audio", HttpMethod.POST,"文本转音频"),
    INFO("/info", HttpMethod.GET,"获取应用基本信息"),
    META("/meta", HttpMethod.GET,"获取应用元数据"),
    ;

    private final String url;

    private final HttpMethod httpMethod;

    private final String description;

    UrlEnum(String url, HttpMethod httpMethod, String description) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.description = description;
    }
}
