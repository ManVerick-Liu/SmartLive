package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IMqttDao;
import com.MacrohardStudio.service.interfaces.IMqttService;
import com.MacrohardStudio.utilities.mqtt.clients.sender.SenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MqttService implements IMqttService
{
    @Autowired
    private IMqttDao iMqttDao;

    @Autowired
    private SenderClient senderClient;

    public void publish(String command)
    {
        senderClient.publish(command);
    }

    public void mqttMessageHandler(String data)
    {

    }
}
