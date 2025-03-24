package com.smart.service.ai.dify.controller;

import com.smart.framework.ai.dify.api.DifyClient;
import com.smart.framework.ai.dify.api.model.AbstractChatCompletionResponse;
import com.smart.framework.ai.dify.api.request.ChatMessagesRequest;
import com.smart.framework.ai.dify.api.request.DifyConversationListRequest;
import com.smart.framework.ai.dify.api.request.DifyFileUploadRequest;
import com.smart.framework.ai.dify.api.request.DifyMessageHistoryRequest;
import com.smart.framework.ai.dify.api.response.*;
import com.smart.framework.commons.core.message.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author shizhongming
 * 2025/2/13 19:38
 * @since 5.0.0
 */
@RestController
@RequestMapping("ai/dify")
@RequiredArgsConstructor
@Tag(name = "dify AI接口")
public class DifyController {

    private final DifyClient difyClient;

    @Operation(summary = "发送对话消息")
    @PostMapping(path = "chatMessages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AbstractChatCompletionResponse> chatMessagesStream(@RequestBody ChatMessagesRequest request) {
        return this.difyClient.chatMessagesStreaming(request);
    }

    @Operation(summary = "上传文件")
    @PostMapping("upload")
    public Result<DifyFileUploadResponse> upload(DifyFileUploadRequest request) {
        return Result.success(difyClient.uploadFile(request));
    }

    @Operation(summary = "查询会话历史消息")
    @PostMapping("messageHistory")
    public Result<List<DifyMessageHistoryResponse>> messageHistory(@RequestBody DifyMessageHistoryRequest request) {
        return Result.success(difyClient.messageHistory(request));
    }

    @Operation(summary = "获取会话列表")
    @PostMapping("listConversation")
    public Result<DifyConversationListResponse> listConversation(@RequestBody DifyConversationListRequest request) {
        return Result.success(difyClient.conversationList(request));
    }

    @Operation(summary = "获取应用基本信息")
    @PostMapping("info")
    public Result<DifyInfoResponse> info() {
        return Result.success(difyClient.info());
    }

    @Operation(summary = "获取应用参数")
    @PostMapping("parameters")
    public Result<DifyParameterResponse> parameters() {
        return Result.success(difyClient.parameters());
    }

    @Operation(summary = "获取应用元数据")
    @PostMapping("meta")
    public Result<DifyMetaReponse> meta() {
        return Result.success(difyClient.meta());
    }

}
