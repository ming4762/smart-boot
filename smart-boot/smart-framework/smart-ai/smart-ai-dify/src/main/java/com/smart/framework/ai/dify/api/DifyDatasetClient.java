package com.smart.framework.ai.dify.api;

import com.smart.framework.ai.dify.api.request.DifyDatasetDocumentCreateByTextRequest;
import com.smart.framework.ai.dify.api.response.DifyDatasetDocumentCreateResponse;

/**
 * Dify知识库API客户端
 * @author shizhongming
 * 2025/2/15 1:32
 * @since 5.0.0
 */
public interface DifyDatasetClient {

    /**
     * 通过文本创建文档
     * 此接口基于已存在知识库，在此知识库的基础上通过文本创建新的文档
     * @param request 请求参数
     * @return 创建结果
     */
    DifyDatasetDocumentCreateResponse createDocumentByText(DifyDatasetDocumentCreateByTextRequest request);
}
