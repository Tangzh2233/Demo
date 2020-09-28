package com.edu.config.message;

import com.edu.controller.interceptor.CustomWebSocketInterceptor;
import com.edu.service.message.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author Tangzhihao
 * @date 2017/10/11
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WebSocketHandler webSocketHandler;
    @Resource
    private CustomWebSocketInterceptor customWebSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/websocket", "sockjs/websocket")
                .addInterceptors(customWebSocketInterceptor);
    }
}
