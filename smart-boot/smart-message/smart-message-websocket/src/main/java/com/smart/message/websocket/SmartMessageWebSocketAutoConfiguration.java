package com.smart.message.websocket;

import com.smart.message.websocket.sender.SmartMessageWebSocketSender;
import com.smart.message.websocket.server.WebSocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author zhongming4762
 * 2023/5/26
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
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
}
