package com.smart.commons.core.constants;

/**
 * Mapper 扫描路径
 * @author ShiZhongMing
 * 2022/5/18
 * @since 2.0.0
 */
public interface MapperPackageConstants {

    /**
     * 文档模块 mapper扫描路径
     */
    String MODULE_DOCUMENT = "com.smart.module.document.mapper";

    /**
     * 系统模块 mapper扫描路径
     */
    String MODULE_SYSTEM = "com.smart.system.mapper";

    /**
     * 代码生成器模块
     */
    String DATABASE_GENERATOR = "com.smart.db.generator.mapper";

    /**
     * 监控服务端mapper扫描
     */
    String MONITOR_SERVER = "com.smart.monitor.server.manager.mapper";

    /**
     * 文件管理模块 mapper扫描
     */
    String MODULE_FILE = "com.smart.file.manager.mapper";

    /**
     * license模块
     */
    String MODULE_LICENSE = "";

    /**
     * 短信管理模块
     */
    String MODULE_SMS = "com.smart.sms.manager.mapper";

    /**
     * 消息模块
     */
    String MODULE_MESSAGE = "com.smart.message.mapper";
}
