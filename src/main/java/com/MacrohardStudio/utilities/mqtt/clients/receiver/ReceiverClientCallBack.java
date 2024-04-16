package com.MacrohardStudio.utilities.mqtt.clients.receiver;

import com.MacrohardStudio.dao.IMqttDao;
import com.MacrohardStudio.model.enums.LogTitle;
import com.MacrohardStudio.service.interfaces.IMqttService;
import com.MacrohardStudio.utilities.mqtt.utils.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;


@Slf4j
@Component
public class ReceiverClientCallBack implements MqttCallbackExtended {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverClientCallBack.class);

    @Autowired
    private ReceiverClient receiverClient;

    @Autowired
    private IMqttService iMqttService;

    @Autowired
    private MqttProperties mqttProperties;

    /**
     * 客户端断开后触发
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        //logger.info(LogTitle.MQTT.toString() + " 与EMQX服务器连接断开，可以做重连");
        if (ReceiverClient.client == null || !ReceiverClient.client.isConnected()) {
            //logger.info(LogTitle.MQTT.toString() + " 与EMQX服务器成功重新连接");
   
        }
    }

    /**
     * 客户端收到消息触发
     *
     * @param topic       主题
     * @param mqttMessage 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String dataString =  new String(mqttMessage.getPayload());

        logger.info(LogTitle.MQTT.toString() + " 消息来自主题：{} Qos：{} 消息内容：{}", topic, mqttMessage.getQos(), dataString);

        JSONObject data = new JSONObject(dataString);

        iMqttService.mqttMessageHandler(data);
    }

    /**
     * 发布消息成功
     *
     * @param token token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
        /*String[] topics = token.getTopics();
        for (String topic : topics) {
            logger.info("向主题：" + topic + "发送消息成功！");
        }
        try {
            MqttMessage message = token.getMessage();
            byte[] payload = message.getPayload();
            String s = new String(payload, "UTF-8");
            logger.info("消息的内容是：" + s);
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 连接emq服务器后触发
     *
     * @param b
     * @param s
     */
    @Override
    public void connectComplete(boolean b, String s) {
        //logger.info(LogTitle.MQTT.toString() + " 客户端id：{} 连接成功", ReceiverClient.client.getClientId());

        //订阅主题
        receiverClient.subscribe("publish", mqttProperties.getQos());
    }
}
