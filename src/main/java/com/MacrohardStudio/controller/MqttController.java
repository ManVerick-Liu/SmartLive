package com.MacrohardStudio.controller;

import com.MacrohardStudio.utilities.mqtt.clients.receiver.ReceiverClient;
import com.MacrohardStudio.utilities.mqtt.clients.sender.SenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/mqtt")
public class MqttController
{
    @Autowired
    private SenderClient senderClient;

    @Autowired
    private ReceiverClient receiverClient;

    @GetMapping(value = "/publish")
    public void publish() throws JSONException
    {
        JSONObject command = new JSONObject();
        command.put("1101","0");
        senderClient.publish("subtopic", command.toString());
    }
}
