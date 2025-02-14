package com.smart.framework.ai.dify.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传请求
 * @author shizhongming
 * 2025/2/10 20:37
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyFileUploadRequest {

    /**
     * 用户标识，用于定义终端用户的身份，必须和发送消息接口传入 user 保持一致。
     */
    private String user;

    /**
     * 文件
     */
    private MultipartFile file;
}
