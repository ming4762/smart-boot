package com.smart.crud.plus.configuration;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;

/**
 * @author shizhongming
 * 2024/4/20 19:39
 * @since 3.0.0
 */
public class LanguageDriverChangeConfigurationCustomizer implements ConfigurationCustomizer {
    /**
     * Customize the given a {@link MybatisConfiguration} object.
     *
     * @param configuration the configuration object to customize
     */
    @Override
    public void customize(MybatisConfiguration configuration) {
        configuration.setDefaultScriptingLanguage(SmartMybatisXMLLanguageDriver.class);
    }
}
