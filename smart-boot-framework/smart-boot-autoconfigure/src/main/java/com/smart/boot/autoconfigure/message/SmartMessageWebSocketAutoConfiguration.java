package com.smart.boot.autoconfigure.message;

import com.smart.framework.message.websocket.filter.WebsocketFilter;
import com.smart.framework.message.websocket.sender.SmartMessageWebSocketSender;
import com.smart.framework.message.websocket.server.WebSocket;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartMessageWebSocketSender.class)
public class SmartMessageWebSocketAutoConfiguration {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebSocket webSocket() {
        return new WebSocket();
    }


    @Bean
    public SmartMessageWebSocketSender smartMessageWebSocketSender(WebSocket webSocket) {
        return new SmartMessageWebSocketSender(webSocket);
    }

    @Bean
    public WebsocketFilter websocketFilter() {
        return new WebsocketFilter();
    }

    @Bean
    public FilterRegistrationBean<WebsocketFilter> webSocketfilterRegistrationBean(WebsocketFilter filter) {
        FilterRegistrationBean<WebsocketFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/websocket/*");
        return filterRegistrationBean;
    }
}
