package com.smart.boot.autoconfigure.ai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shizhongming
 * 2025/5/13 14:25
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "smart.ai.open-ai")
@Getter
@Setter
public class SmartOpenAiProperties implements Serializable {

    private List<ChatModelProperties> chatModelList = new ArrayList<>(0);

    @Getter
    @Setter
    public static class ChatModelProperties implements Serializable {
        private String apiKey;
        private String baseUrl;
        private String name;
        private String modelName;
    }
}
