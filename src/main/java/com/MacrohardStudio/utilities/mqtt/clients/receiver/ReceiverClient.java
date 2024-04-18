package com.MacrohardStudio.utilities.mqtt.clients.receiver;

import com.MacrohardStudio.model.enums.LogTitle;
import com.MacrohardStudio.utilities.mqtt.utils.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class ReceiverClient
{
    private static final Logger logger = LoggerFactory.getLogger(ReceiverClient.class);

    @Autowired
    private ReceiverClientCallBack receiverClientCallBack;

    @Autowired
    private MqttProperties mqttProperties;

    public static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        ReceiverClient.client = client;
    }

    public String clientId;
    /**
     * 客户端连接
     */
    public void connect() {
        MqttClient client;
        try
        {
            // 生成随机字符串加到客户端id上，防止重复的客户端id出现
            clientId = "BackendReceiverClient" + generateRandomString();

            client = new MqttClient(mqttProperties.getHostUrl(), clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttProperties.getUsername());
            options.setPassword(mqttProperties.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProperties.getTimeout());
            options.setKeepAliveInterval(mqttProperties.getKeepAlive());
            options.setAutomaticReconnect(false);
            options.setCleanSession(mqttProperties.getCleanSession());
            ReceiverClient.setClient(client);
            try
            {
                // 设置回调
                client.setCallback(receiverClientCallBack);
                client.connect(options);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 重新连接
     */
    public void reconnection() {
        try {
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public boolean subscribe(String topic, int qos) {
        logger.info(LogTitle.MQTT.toString() + " 客户端id：{} 开始订阅主题：{}", clientId, topic);
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 取消订阅某个主题
     *
     * @param topic
     */
    public void unsubscribe(String topic) {
        //logger.info(LogTitle.MQTT.toString() + " 取消订阅主题：" + topic);
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomString()
    {
        // 可以包含的字符集合
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        // 随机数生成器
        Random random = new Random();
        // 生成随机字符串
        StringBuilder sb = new StringBuilder("#");
        for (int i = 0; i < 5; i++) {
            // 从字符集合中随机选择一个字符
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            sb.append(randomChar);
        }
        return sb.toString();
    }
}

