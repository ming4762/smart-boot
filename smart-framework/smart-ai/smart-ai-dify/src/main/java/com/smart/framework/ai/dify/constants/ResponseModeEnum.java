package com.smart.framework.ai.dify.constants;

import lombok.Getter;

/**
 * streaming 流式模式（推荐）。基于 SSE（Server-Sent Events）实现类似打字机输出方式的流式返回。
 * blocking 阻塞模式，等待执行完毕后返回结果。（请求若流程较长可能会被中断）。 由于 Cloudflare 限制，请求会在 100 秒超时无返回后中断。 注：Agent模式下不允许blocking。
 * @author shizhongming
 * 2025/2/8 19:13
 * @since 5.0.0
 */
@Getter
public enum ResponseModeEnum implements EnumValue {

    /**
     * streaming 流式模式（推荐）。基于 SSE（Server-Sent Events）实现类似打字机输出方式的流式返回。
     */
    STREAMING("streaming"),

    /**
     * blocking 阻塞模式，等待执行完毕后返回结果。（请求若流程较长可能会被中断）。 由于 Cloudflare 限制，请求会在 100 秒超时
     * 无返回后中断。 注：Agent模式下不允许blocking。
     */
    BLOCKING("blocking"),
    ;

    private final String value;

    ResponseModeEnum(String value) {
        this.value = value;
    }
}
