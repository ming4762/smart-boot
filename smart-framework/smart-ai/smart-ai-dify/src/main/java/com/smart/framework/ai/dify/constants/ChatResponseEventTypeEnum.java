package com.smart.framework.ai.dify.constants;

import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.api.response.*;
import lombok.Getter;

/**
 * 对话消息 响应事件类型
 * @author shizhongming
 * 2025/2/8 17:02
 * @since 5.0.0
 */
@Getter
public enum ChatResponseEventTypeEnum implements EnumValue {

    /**
     * 完整的文本以分块的方式输出
     */
    MESSAGE("message", ChatResponseMessageEvent.class),
    /**
     * Agent模式下返回文本块事件，即：在Agent模式下，文章的文本以分块的方式输出（仅Agent模式下使用）
     */
    AGENT_MESSAGE("agent_message", ChatResponseAgentMessageEvent.class),
    /**
     * Agent模式下有关Agent思考步骤的相关内容，涉及到工具调用（仅Agent模式下使用）e
     */
    AGENT_THOUGHT("agent_thought", ChatResponseAgentThoughtEvent.class),
    /**
     * 文件事件，表示有新文件需要展示
     */
    MESSAGE_FILE("message_file", ChatResponseMessageFileEvent.class),
    /**
     * 消息结束事件，收到此事件则代表流式返回结束
     */
    MESSAGE_END("message_end", ChatResponseMessageEndEvent.class),
    /**
     * TTS 音频流事件，即：语音合成输出。内容是Mp3格式的音频块，使用 base64 编码后的字符串，播放的时候直接解码即可。(开启自动播放才有此消息)
     */
    TTS_MESSAGE("tts_message", ChatResponseTtsMessageEvent.class),
    /**
     * TTS 音频流结束事件，收到这个事件表示音频流返回结束。
     */
    TTS_MESSAGE_END("tts_message_end", ChatResponseTtsMessageEndEvent.class),
    /**
     * 消息内容替换事件。 开启内容审查和审查输出内容时，若命中了审查条件，则会通过此事件替换消息内容为预设回复。
     */
    MESSAGE_REPLACE("message_replace", ChatResponseMessageReplaceEvent.class),
    /**
     * 流式输出过程中出现的异常会以 stream event 形式输出，收到异常事件后即结束。
     */
    ERROR("error", ChatResponseErrorEvent.class),
    /**
     * 每 10s 一次的 ping 事件，保持连接存活。
     */
    PING("ping", ChatResponsePingEvent.class),
    ;

    private final String value;

    private final Class<? extends AbstractChatCompletionResponse> modelClass;

    ChatResponseEventTypeEnum(String value, Class<? extends AbstractChatCompletionResponse> modelClass) {
        this.value = value;
        this.modelClass = modelClass;
    }

    public static ChatResponseEventTypeEnum getByValue(String value) {
        for (ChatResponseEventTypeEnum eventType : ChatResponseEventTypeEnum.values()) {
            if (eventType.getValue().equals(value)) {
                return eventType;
            }
        }
        return null;
    }
}
