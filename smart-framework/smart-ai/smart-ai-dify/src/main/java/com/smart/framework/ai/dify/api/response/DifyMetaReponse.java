package com.smart.framework.ai.dify.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取应用Meta信息
 * @author shizhongming
 * 2025/2/10 19:42
 * @since 5.0.0
 */
@Getter
@Setter
public class DifyMetaReponse {

    @JsonProperty("tool_icons")
    private ToolIcons toolIcons;

    @Getter
    @Setter
    public static class ToolIcons {
        private String dalle2;

        @JsonProperty("api_tool")
        private ApiTool apiTool;
    }

    @Getter
    @Setter
    public static class ApiTool {

        /**
         * (string) hex格式的背景色
         */
        private String background;

        /**
         * emoji
         */
        private String content;
    }
}
