package com.smart.framework.ai.dify.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

/**
 * 获取会话列表参数
 * @author shizhongming
 * 2025/2/14 20:33
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyConversationListRequest {

    @NonNull
    private String user;

    /**
     * （选填）当前页最后面一条记录的 ID，默认 null
     */
    @JsonProperty("first_id")
    private String lastId;

    /**
     * （选填）一次请求返回多少条记录，默认 20 条，最大 100 条，最小 1 条。
     */
    private Long limit;

    /**
     * （选填）排序字段，默认 -updated_at(按更新时间倒序排列)
     * 可选值：created_at, -created_at, updated_at, -updated_at
     * 字段前面的符号代表顺序或倒序，-代表倒序
     */
    @JsonProperty("sort_by")
    private String sortBy;
}
