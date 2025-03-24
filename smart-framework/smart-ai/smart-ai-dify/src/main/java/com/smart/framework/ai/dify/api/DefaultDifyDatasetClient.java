package com.smart.framework.ai.dify.api;

import com.smart.framework.ai.dify.api.request.DifyDatasetDocumentCreateByTextRequest;
import com.smart.framework.ai.dify.api.response.DifyDatasetDocumentCreateResponse;
import com.smart.framework.ai.dify.config.ClientConfig;
import com.smart.framework.ai.dify.constants.UrlEnum;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

/**
 * Dify知识库API客户端
 * @author shizhongming
 * 2025/2/15 15:51
 * @since 5.0.0
 */
public class DefaultDifyDatasetClient extends CommonApi implements DifyDatasetClient {

    public DefaultDifyDatasetClient(ClientConfig clientConfig) {
        super(clientConfig);
    }

    /**
     * 通过文本创建文档
     * 此接口基于已存在知识库，在此知识库的基础上通过文本创建新的文档
     *
     * @param request 请求参数
     * @return 创建结果
     */
    @Override
    public DifyDatasetDocumentCreateResponse createDocumentByText(DifyDatasetDocumentCreateByTextRequest request) {
        String url = String.format(this.getApiUrl(UrlEnum.DATASET_DOCUMENT_CREATE_BY_TEXT), request.getDatasetId());
        return this.doRequest(
                url,
                UrlEnum.DATASET_DOCUMENT_CREATE_BY_TEXT.getHttpMethod(),
                MediaType.APPLICATION_JSON_VALUE,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}
