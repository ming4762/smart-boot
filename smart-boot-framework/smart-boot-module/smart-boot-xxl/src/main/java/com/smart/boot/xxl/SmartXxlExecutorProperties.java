package com.smart.boot.xxl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XXL执行器配置文件
 * @author shizhongming
 * 2024/6/26 20:49
 * @since 3.0.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.xxl")
public class SmartXxlExecutorProperties {

    /**
     * 调度器配置
     */
    private Admin admin = new Admin();

    /**
     * 调度器配置
     */
    private Executor executor = new Executor();

    @Getter
    @Setter
    public static class Admin {
        /**
         * 调度器地址
         */
        private String addresses;

        /**
         * token
         */
        private String accessToken;
    }

    /**
     * 执行器配置
     */
    @Getter
    @Setter
    public static class Executor {
        /**
         * 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
         */
        private String appName;

        /**
         * 执行器注册 [选填]：优先使用该配置作为注册地址，为空时使用内嵌服务 ”IP:PORT“ 作为注册地址。从而更灵活的支持容器类型执行器动态IP和动态映射端口问题。
         */
        private String address;

        /**
         * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"；
         */
        private String ip;

        /**
         * 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
         */
        private Integer port;

        /**
         * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
         */
        private String logPath;

        /**
         * 执行器日志文件保存天数 [选填] ： 过期日志自动清理, 限制值大于等于3时生效; 否则, 如-1, 关闭自动清理功能；
         */
        private Integer logRetentionDays;
    }
}
