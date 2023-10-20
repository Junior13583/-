package com.example.junior.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpointConfig;


/**
* @Description: websocket配置类
* @Author: Junior
* @Date: 2023/6/10
*/
@Configuration
public class WebSocketConfigurator extends ServerEndpointConfig.Configurator {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
