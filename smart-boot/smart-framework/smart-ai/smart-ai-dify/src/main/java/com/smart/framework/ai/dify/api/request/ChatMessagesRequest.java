package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smart.framework.ai.dify.constants.ChatFileTypeEnum;
import com.smart.framework.ai.dify.constants.FileTransferMethodEnum;
import com.smart.framework.ai.dify.json.EnumValueJson;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 发送对话消息请求
 * @author shizhongming
 * 2025/2/8 19:09
 * @since 5.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessagesRequest {

    /**
     * 用户输入/提问内容
     */
    @NonNull
    private String query;

    /**
     * 允许传入 App 定义的各变量值。 inputs 参数包含了多组键值对（Key/Value pairs），每组的键对应一个特定变量，每组的值则是该变量的具体值。 默认 {}
     */
    @NonNull
    private Map<String, Object> inputs;

    /**
     * 用户标识，用于定义终端用户的身份，方便检索、统计。 由开发者定义规则，需保证用户标识在应用内唯一。
     */
    @NonNull
    private String user;

    /**
     * 上传的文件。
     */
    private List<ChatMessagesFile> files;

    /**
     * （选填）自动生成标题，默认 true。 若设置为 false，则可通过调用会话重命名接口并设置 auto_generate 为 true 实现异步生成标题。
     */
    @JsonProperty("auto_generate_name")
    private Boolean autoGenerateName;


    /**
     * 上传的文件。
     */
    @Getter
    @Setter
    public static class ChatMessagesFile {

        /**
         * 支持类型：图片 image（目前仅支持图片格式） 。
         */
        @NonNull
        @JsonSerialize(using = EnumValueJson.EnumValueSerializer.class)
        private ChatFileTypeEnum type;

        /**
         * 传输方式
         */
        @NonNull
        @JsonProperty("transfer_method")
        @JsonSerialize(using = EnumValueJson.EnumValueSerializer.class)
        private FileTransferMethodEnum transferMethod;

        /**
         * 图片地址。（仅当传递方式为 remote_url 时）。
         */
        private String url;

        /**
         * 上传文件 ID。（仅当传递方式为 local_file 时）。
         */
        @JsonProperty("upload_file_id")
        private String uploadFileId;
    }
}
