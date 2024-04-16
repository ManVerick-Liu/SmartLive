package com.MacrohardStudio.utilities.mqtt.clients.sender;

import com.MacrohardStudio.model.enums.LogTitle;
import com.MacrohardStudio.utilities.mqtt.utils.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SenderClient
{

    private static final Logger logger = LoggerFactory.getLogger(SenderClient.class);

    @Autowired
    private SenderClientCallBack senderClientCallBack;

    @Autowired
    private MqttProperties mqttProperties;

    public static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        SenderClient.client = client;
    }

    /**
     * 客户端连接
     */
    public void connect() {
        MqttClient client;
        try
        {
            client = new MqttClient(mqttProperties.getHostUrl(), "BackendSendClient", new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(mqttProperties.getUsername());
            options.setPassword(mqttProperties.getPassword().toCharArray());
            options.setConnectionTimeout(mqttProperties.getTimeout());
            options.setKeepAliveInterval(mqttProperties.getKeepAlive());
            options.setAutomaticReconnect(mqttProperties.getReconnect());
            options.setCleanSession(mqttProperties.getCleanSession());
            SenderClient.setClient(client);
            try
            {
                // 设置回调
                client.setCallback(senderClientCallBack);
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
     * 发布消息
     * 主题格式： server:report:$orgCode(参数实际使用机构代码)
     *
     * @param topic 订阅的主题
     * @param pushMessage 消息体
     */
    public void publish(String topic, String pushMessage)
    {
        MqttMessage message = new MqttMessage();
        message.setQos(mqttProperties.getQos());
        message.setRetained(mqttProperties.getRetained());
        message.setPayload(pushMessage.getBytes());
        MqttDeliveryToken token;
        connect();
        try
        {
            client.publish(topic, message);
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    public void publish(String pushMessage)
    {
        publish("subtopic", pushMessage);
    }

    /*public void publish(String topic, JSONObject pushMessage)
    {
        MqttMessage message = new MqttMessage();
        message.setQos(mqttProperties.getQos());
        message.setRetained(mqttProperties.getRetained());
        message.setPayload(pushMessage.getBytes());
        MqttDeliveryToken token;
        connect();
        try
        {
            client.publish(topic, message);
        } catch (MqttException e)
        {
            e.printStackTrace();
        }
    }*/

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public boolean subscribe(String topic, int qos) {
        logger.info(LogTitle.MQTT.toString() + " 开始订阅主题：" + topic);
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
}

