package com.smart.boot.autoconfigure.ai.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.function.Supplier;

/**
 * 根据配置文件注入所有OpenAiChatModel
 * @author shizhongming
 * 2025/5/13 14:54
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class ChatModelBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private SmartOpenAiProperties openAiProperties;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        for (SmartOpenAiProperties.ChatModelProperties chatModel : this.openAiProperties.getChatModelList()) {
            Supplier<OpenAiChatModel> supplier = () -> OpenAiChatModel.builder()
                    .apiKey(chatModel.getApiKey())
                    .baseUrl(chatModel.getBaseUrl())
                    .modelName(chatModel.getModelName())
                    .build();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(OpenAiChatModel.class, supplier);
            registry.registerBeanDefinition(chatModel.getName(), beanDefinitionBuilder.getBeanDefinition());
        }
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.openAiProperties = Binder.get(environment).bind("smart.ai.open-ai", SmartOpenAiProperties.class).orElse(null);
    }
}
