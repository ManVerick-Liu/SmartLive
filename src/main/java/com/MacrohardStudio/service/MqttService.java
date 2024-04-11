package com.MacrohardStudio.service;

import com.MacrohardStudio.dao.IMqttDao;
import com.MacrohardStudio.service.interfaces.IMqttService;
import com.MacrohardStudio.utilities.mqtt.clients.sender.SenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MqttService implements IMqttService
{
    @Autowired
    private IMqttDao iMqttDao;

    @Autowired
    private SenderClient senderClient;

    public void publish(String device_mac_address, String command) throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(device_mac_address, command);
        senderClient.publish("subtopic", jsonObject.toString());
    }

    public void mqttMessageHandler(String data)
    {

    }
}
