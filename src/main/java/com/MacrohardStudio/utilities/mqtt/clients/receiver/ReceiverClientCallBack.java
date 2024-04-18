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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@Slf4j
@Component
public class ReceiverClientCallBack implements MqttCallbackExtended
{
    private static final Logger logger = LoggerFactory.getLogger(ReceiverClientCallBack.class);

    @Autowired
    private ReceiverClient receiverClient;

    @Autowired
    private IMqttService iMqttService;

    @Autowired
    private MqttProperties mqttProperties;

    private final Queue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private volatile boolean isProcessing = false;

    /**
     * 客户端断开后触发
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable)
    {
        //logger.info(LogTitle.MQTT.toString() + " 与EMQX服务器连接断开，可以做重连");
        /*if (ReceiverClient.client == null || !ReceiverClient.client.isConnected())
        {
            //logger.info(LogTitle.MQTT.toString() + " 与EMQX服务器成功重新连接");
   
        }*/
        receiverClient.unsubscribe("publish");
        receiverClient.connect();
    }

    /**
     * 客户端收到消息触发
     *
     * @param topic       主题
     * @param mqttMessage 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
    {
        String dataString =  new String(mqttMessage.getPayload());

        logger.info(LogTitle.MQTT.toString() + " 客户端id：{} 收到来自主题 {} 的消息：{}", receiverClient.clientId, topic, dataString);

        // 将收到的消息放入队列
        messageQueue.offer(dataString);

        // 如果当前没有处理消息的线程在运行，则启动一个新的线程来处理队列中的消息
        if (!isProcessing) {
            synchronized (this) {
                if (!isProcessing) {
                    isProcessing = true;
                    processQueue();
                }
            }
        }

        //JSONObject data = new JSONObject(dataString);
        //iMqttService.mqttMessageHandler(data);
    }

    /**
     * 发布消息成功
     *
     * @param token token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {

    }

    /**
     * 连接emq服务器后触发
     *
     * @param b
     * @param s
     */
    @Override
    public void connectComplete(boolean b, String s)
    {
        logger.info(LogTitle.MQTT.toString() + " 客户端id：{} 连接成功", ReceiverClient.client.getClientId());

        //订阅主题
        receiverClient.subscribe("publish", mqttProperties.getQos());
    }

    // 处理消息队列的方法
    private void processQueue() {
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.poll(); // 从队列中取出消息进行处理
            try {
                JSONObject data = new JSONObject(message);
                iMqttService.mqttMessageHandler(data); // 处理消息的逻辑
            } catch (Exception e) {
                logger.error("处理 MQTT 消息时出错：" + e.getMessage());
            }
        }
        isProcessing = false; // 处理完毕后将标志位重置为 false，表示可以再次处理新的消息
    }
}
