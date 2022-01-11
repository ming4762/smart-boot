package com.smart.i18n;

import com.smart.i18n.cache.MemoryResourceCache;
import com.smart.i18n.cache.ResourceCache;
import com.smart.i18n.format.DefaultMessageFormat;
import com.smart.i18n.format.MessageFormat;
import com.smart.i18n.reader.ResourceBundleResourceReader;
import com.smart.i18n.reader.ResourceReader;
import com.smart.i18n.resolve.AcceptHeaderAndUserLocaleResolver;
import com.smart.i18n.source.AutoReloadMessageSource;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author shizhongming
 * 2020/5/12 5:57 下午
 */
@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
public class SmartI18nAutoConfiguration {


    @Bean
    @ConfigurationProperties("smart.i18n")
    public I18nProperties i18nProperties() {
        return new I18nProperties();
    }

    /**
     * 初始化I18N 工具类
     * @return I18nUtilsInit
     */
    @Bean
    public I18nUtilsInit i18nUtilsInit() {
        return new I18nUtilsInit();
    }

    /**
     * 创建 localeResolver
     * @param properties 参数
     * @return localeResolver
     */
    @Bean
    @ConditionalOnMissingBean(LocaleResolver.class)
    public LocaleResolver localeResolver(I18nProperties properties) {
        final AcceptHeaderAndUserLocaleResolver localeResolver =  new AcceptHeaderAndUserLocaleResolver();
        localeResolver.setDefaultLocale(properties.getDefaultLocale());
        return localeResolver;
    }

    /**
     * 创建 ResourceCache
     * @return MemoryResourceCache
     */
    @Bean
    @ConditionalOnMissingBean(ResourceCache.class)
    public ResourceCache memoryResourceCache() {
        return new MemoryResourceCache();
    }

    /**
     * 创建messageFormat
     * @return MessageFormat
     */
    @Bean
    @ConditionalOnMissingBean(MessageFormat.class)
    public MessageFormat messageFormat() {
        return new DefaultMessageFormat();
    }

    /**
     * 创建 ResourceReader
     * @param properties 参数
     * @return ResourceReader
     */
    @Bean
    @ConditionalOnMissingBean(ResourceReader.class)
    public ResourceReader resourceReader(I18nProperties properties) {
        final ResourceBundleResourceReader resourceReader = new ResourceBundleResourceReader();
        resourceReader.setEncoding(properties.getEncoding());
        if (!org.apache.commons.lang3.StringUtils.isEmpty(properties.getBasename())) {
            resourceReader.addBasename(StringUtils.commaDelimitedListToSet(StringUtils.trimAllWhitespace(properties.getBasename())));
        }
        return resourceReader;
    }


    /**
     * 创建message Source
     * @param properties 参数
     * @return MessageSource
     */
    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource(I18nProperties properties) {
        AutoReloadMessageSource autoReloadMessageSource =  new AutoReloadMessageSource();
        autoReloadMessageSource.setDuration(properties.getCache().getDuration());
        return autoReloadMessageSource;
    }
}
