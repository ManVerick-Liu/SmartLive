package com.MacrohardStudio.utilities.webSocket;

import com.MacrohardStudio.utilities.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Slf4j
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator
{
    /**
     * 注册ServerEndpointExporter，它会自动扫描带有@ServerEndpoint注解声明的Websocket Endpoint(端点)，
     * 并注册成为Websocket bean。
     * 要注意，如果项目使用外置的servlet容器，而不是直接使用springboot内置容器的话，就不要注入ServerEndpointExporter，
     * 因为它将由容器自己提供和管理。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }

    /**
     * 建立握手时，连接前的操作
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response)
    {
        //从请求的Header中获取用户标识符
        /*String token = request.getHeaders().get("Authorization").toString();

        String user_id = JwtUtils.verify(token);

        try {
            JwtUtils.verify(token);
        } catch (Exception e) {
            log.info("连接非法",e.getMessage());
            return;
        }

        //设置用户标识符到config中
        config.getUserProperties().put("token", token);
        super.modifyHandshake(config, request, response);*/
    }
}
