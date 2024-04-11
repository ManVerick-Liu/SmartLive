package com.MacrohardStudio.controller;

import com.MacrohardStudio.annotation.WithoutJWT;
import com.MacrohardStudio.service.interfaces.IMqttService;
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
    private IMqttService iMqttService;
    @WithoutJWT
    @GetMapping(value = "/publish")
    public void publish(String device_mac_address, String command) throws JSONException
    {
        iMqttService.publish(device_mac_address, command);
    }

}
