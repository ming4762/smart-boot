package com.smart.monitor.server.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Objects;

/**
 * 服务端参数
 * @author shizhongming
 * 2021/3/21 11:17 下午
 */
@ConfigurationProperties("smart.monitor.server")
@Getter
@Setter
public class MonitorServerProperties implements InitializingBean {


    private Client client = new Client();

    private Manager manager = new Manager();

    private Notify notify = new Notify();

    @Override
    public void afterPropertiesSet() throws Exception {
        final MailNotify mail = this.notify.getMail();
        if (Objects.equals(mail.enable, Boolean.TRUE)) {
            Assert.notNull(mail.from, "from email is null");
        }
    }

    @Getter
    @Setter
    public static class Client {

        /**
         * 检查客户端状态的时间间隔
         */
        private Duration statusInterval = Duration.ofMillis(10000L);

        /**
         * 客户端下线的时间间隔
         */
        private Duration offlineInterval = Duration.ofMillis(50000L);

        /**
         * 默认的客户端连接token信息
         */
        private String defaultToken;
    }

    @Getter
    @Setter
    public static class Manager {
        /**
         * 是否开启开发模式
         */
        private Boolean develop = false;
    }

    /**
     * 通知配置
     */
    @Getter
    @Setter
    public static class Notify {

        private MailNotify mail = new MailNotify();
    }

    /**
     * 邮件通知配置
     */
    @Getter
    @Setter
    public static class MailNotify {

        private Boolean enable = Boolean.FALSE;

        /**
         * 发件人
         */
        private String from;

        /**
         * 默认的通知事件
         */
        private String defaultEvent = "UP,DOWN,OFFLINE,ONLINE";

        /**
         * 默认的收件人
         */
        private String defaultTo;

        private String defaultCc;

        /**
         * 模板路径
         */
        private String template = "classpath:/template/notify/mail/event-changed.html";
    }
}
