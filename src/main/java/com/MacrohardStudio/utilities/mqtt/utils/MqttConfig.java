package com.MacrohardStudio.utilities.mqtt.utils;

import com.MacrohardStudio.utilities.mqtt.clients.receiver.ReceiverClient;
import com.MacrohardStudio.utilities.mqtt.clients.sender.SenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Autowired
    private SenderClient senderClient;

    @Autowired
    private ReceiverClient receiverClient;

    /**
     * 订阅mqtt
     *
     * @return
     */
    @Conditional(MqttCondition.class)
    @Bean
    public SenderClient getMqttPushClient()
    {
        senderClient.connect();
        receiverClient.connect();
        return senderClient;
    }
}

