package com.smart.boot.crud;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.smart.framework.crud.plus.configuration.SmartMybatisXMLLanguageDriver;

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
