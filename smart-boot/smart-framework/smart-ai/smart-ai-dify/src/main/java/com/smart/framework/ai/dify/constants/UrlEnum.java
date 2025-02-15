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
    PARAMETERS("/parameters", HttpMethod.GET,"获取应用参数"),
    META("/meta", HttpMethod.GET,"获取应用元数据"),

    /**
     * 知识库
     */
    DATASET_DOCUMENT_CREATE_BY_TEXT("/datasets/%s/document/create-by-text", HttpMethod.POST, "通过文本创建文档"),
    DATASET_DOCUMENT_CREATE_BY_FILE("/datasets/%s/document/create-by-file", HttpMethod.POST, "通过文件创建文档"),
    DATASET_CREATE_EMPTY("/datasets", HttpMethod.POST, "创建空知识库"),
    DATASET_LIST("/datasets", HttpMethod.GET, "获取知识库列表"),
    DATASET_DELETE("/datasets/%s", HttpMethod.DELETE, "删除知识库"),
    DATASET_DOCUMENT_UPDATE_BY_TEXT("/datasets/%s/document/%s/update-by-text", HttpMethod.POST, "通过文本更新文档"),
    DATASET_DOCUMENT_UPDATE_BY_FILE("/datasets/%s/document/%s/update-by-file", HttpMethod.POST, "通过文件更新文档"),
    DATASET_DOCUMENT_INDEXING_STATUS("/datasets/%s/document/%s/indexing-status", HttpMethod.GET, "获取文档嵌入状态（进度）"),
    DATASET_DOCUMENT_DELETE("/datasets/%s/document/%s", HttpMethod.DELETE, "删除文档"),
    DATASET_DOCUMENT_LIST("/datasets/%s/documents", HttpMethod.GET, "获取文档列表"),
    DATASET_DOCUMENT_SEGMENTS_ADD("/datasets/%s/document/%s/segments", HttpMethod.POST, "新增分段"),
    DATASET_DOCUMENT_SEGMENTS_LIST("/datasets/%s/document/%s/segments", HttpMethod.GET, "查询文档分段"),
    DATASET_DOCUMENT_SEGMENTS_DELETE("/datasets/%s/document/%s/segments/%s", HttpMethod.DELETE, "删除分段"),
    DATASET_DOCUMENT_SEGMENTS_UPDATE("/datasets/%s/document/%s/segments/%s", HttpMethod.POST, "更新文档分段"),
    DATASET_DOCUMENT_GET_FILE("/datasets/%s/document/%s/upload-file", HttpMethod.GET, "获取上传文件"),
    DATASET_RETRIEVE("/datasets/%s/retrieve", HttpMethod.POST, "检索知识库"),
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
